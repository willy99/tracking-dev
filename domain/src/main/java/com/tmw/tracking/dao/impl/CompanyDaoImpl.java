package com.tmw.tracking.dao.impl;

import com.google.common.collect.Lists;
import com.google.inject.Singleton;
import com.tmw.tracking.Transaction;
import com.tmw.tracking.dao.CompanyDao;
import com.tmw.tracking.dao.RoleDao;
import com.tmw.tracking.dao.UserDao;
import com.tmw.tracking.domain.SearchCriteria;
import com.tmw.tracking.entity.Company;
import com.tmw.tracking.entity.User;
import com.tmw.tracking.entity.paging.Page;
import com.tmw.tracking.entity.paging.PageQuery;
import com.tmw.tracking.entity.paging.SearchPage;
import com.tmw.tracking.entity.support.UserInfo;
import com.tmw.tracking.utils.DomainUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by pzhelnov on 11/20/2017.
 */
@Singleton
public class CompanyDaoImpl implements CompanyDao {

    private static final Logger logger = LoggerFactory.getLogger(CompanyDaoImpl.class);

    private EntityManager entityManager;
    private UserDao userDao;
    private RoleDao roleDao;

    @Inject
    public CompanyDaoImpl(
            final EntityManager entityManager,
            final UserDao userDao,
            final RoleDao roleDao) {
        this.entityManager = entityManager;
        this.userDao = userDao;
        this.roleDao = roleDao;
    }

    @Override
    public Company getById(Long id) {
        TypedQuery<Company> query = entityManager.createQuery("from Company where id = :id", Company.class);
        query.setParameter("id", id);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Company getByName(String name) {
        TypedQuery<Company> query = entityManager.createQuery("from Company where name = :name", Company.class);
        query.setParameter("name", name);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Transaction
    @Override
    public Company create(Company company) {
        //set defaults
        User admin = company.getAdmin().convert(roleDao);
        admin.setLocale(Locale.ENGLISH);

        entityManager.persist(company);
        admin.setTenant(company); //should set tenant to created company
        company.setAdmin(new UserInfo(userDao.create(admin)));

        return company;
    }

    @Transaction
    @Override
    public Company update(Company company) {
        User admin = company.getAdmin().convert(roleDao);
        admin.setTenant(company);
        userDao.update(admin);
        logger.debug("Updating company " + company.getName());
        return entityManager.merge(company);
    }

    @Override
    public List<Company> getActive() {
        final String hql = "from Company where active = true order by active, name";
        return DomainUtils.getLimitResult(entityManager, Company.class, hql, null, null);
    }

    @Override
    public Page<Company> find(PageQuery pageQuery, SearchCriteria searchCriteria) {
        searchCriteria.setMatcher("*" + searchCriteria.getMatcher() + "*");
        searchCriteria.setMatcherFields(Lists.newArrayList("name"));
        searchCriteria.addEntitiesForSearch(Company.class);
        Page<Company> page = SearchPage.searchFilter(entityManager, pageQuery, searchCriteria);
        List<Long> ids = new ArrayList<Long>();
        for (Company company: page.getContent()) {
            ids.add(company.getId());
        }
        if (ids.isEmpty()) {
            return page;
        }
        List<Object[]> companyInfoList = getCompanyInfoList(ids);
        int i=0;
        for (Company company: page.getContent()) {
            Object[] info = companyInfoList.get(i++);
            company.setActiveUsers(((BigInteger )info[2]).intValue());
            company.setInactiveUsers(((BigInteger )info[3]).intValue());
            company.setOrders(((BigInteger)info[4]).intValue());
        }

        return page;
    }

    private List<Object[]> getCompanyInfoList(List<Long> ids) {
        Query query = entityManager.createNativeQuery(
                "select c.id as id, c.name as name, count(distinct au.id) as activeUsers,count(distinct nu.id) as disabledUsers, count(distinct o.id) as orders " +
                "from tr_company c " +
                "left join tr_user au on au.tenant = c.id and au.active = 'Y' " +
                "left join tr_user nu on nu.tenant = c.id and nu.active = 'N' " +
                "left join tr_order o on o.tenant = c.id " +
                "where c.id in :id " +
                "group by c.id, c.name");
        query.setParameter("id", ids);
        return query.getResultList();
    }


}
