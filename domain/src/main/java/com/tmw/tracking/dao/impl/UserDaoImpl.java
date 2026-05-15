package com.tmw.tracking.dao.impl;

import com.google.common.collect.Lists;
import com.google.inject.Singleton;
import com.tmw.tracking.utils.DomainUtils;
import com.tmw.tracking.Transaction;
import com.tmw.tracking.dao.RoleDao;
import com.tmw.tracking.dao.UserDao;
import com.tmw.tracking.domain.SearchCriteria;
import com.tmw.tracking.entity.Company;
import com.tmw.tracking.entity.Role;
import com.tmw.tracking.entity.User;
import com.tmw.tracking.entity.enums.RoleEnum;
import com.tmw.tracking.entity.paging.Page;
import com.tmw.tracking.entity.paging.PageQuery;
import com.tmw.tracking.entity.paging.SearchPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

@Singleton
public class UserDaoImpl implements UserDao {

    private static final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);

    private EntityManager entityManager;
    private RoleDao roleDao;

    @Inject
    public UserDaoImpl(
            final EntityManager entityManager,
            final RoleDao roleDao) {
        this.roleDao = roleDao;
        this.entityManager = entityManager;
    }

    @Override
    public User getById(final Long id) {
        TypedQuery<User> query = entityManager.createQuery("from User where id = :id", User.class);
        query.setParameter("id", id);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public User getUserByCredentials(final String cred) {
        TypedQuery<User> query = entityManager.createQuery("from User where (email = :email or phone = :phone) and active = 'Y'", User.class);
        query.setParameter("email", cred);
        query.setParameter("phone", cred);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public User getAnyUserByCredentials(final String credentials) {
        TypedQuery<User> query = entityManager.createQuery("from User where (email = :email or phone = :phone )", User.class);
        query.setParameter("email", credentials);
        query.setParameter("phone", credentials);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<User> getUsersByRole(Long roleId) {
        TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u JOIN u.role r WHERE r.id = :roleId", User.class);
        query.setParameter("roleId", roleId);
        try {
            return query.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     *
     * @see UserDao#create(User)
     */
    @Transaction
    @Override
    public User create(final User user) {
        //check for unique id
        User existing = getAnyUserByCredentials(user.getEmail());
        if (existing != null) {
            throw new RuntimeException("User with the email " + user.getEmail() + " already exists (it might be inactive)");
        }
        entityManager.persist(user);
        return user;
    }

    /**
     * {@inheritDoc}
     *
     * @see UserDao#create(User)
     */
    @Transaction
    @Override
    public User update(final User user) {
        return entityManager.merge(user);
    }

    /**
     * {@inheritDoc}
     *
     * @see UserDao#create(User)
     */
    @Transaction
    @Override
    public void delete(final User user) {
        user.setActive(false);
        entityManager.merge(user);
    }


    @Override
    public List<User> getAll() {
        final StringBuilder sql = new StringBuilder();
        sql.append("from User ").append(" order by active, id");
        return DomainUtils.getLimitResult(entityManager, User.class, sql.toString(), null, null);
    }

    @Override
    public User clearRole(String email) {
        User existing = getAnyUserByCredentials(email);
        if (existing == null) {
            return null;
        }
        existing.setRole(null);
        return update(existing);
    }

    @Override
    public Page<User> find(PageQuery pageQuery, SearchCriteria searchCriteria) {
        searchCriteria.setMatcher("*" + searchCriteria.getMatcher() + "*");
        searchCriteria.setMatcherFields(Lists.newArrayList("email", "lastName", "firstName"));
        searchCriteria.addEntitiesForSearch(User.class);
        return SearchPage.searchFilter(entityManager, pageQuery, searchCriteria);
    }



    @Transaction
    @Override
    public User addRoleForUser(Long userId, Role role) {
        User user = getById(userId);
        if (user != null) {
            user.setRole(role);
            return update(user);
        }
        return null;
    }

    @Override
    public Page<User> findUserNotInRole(PageQuery pageQuery, SearchCriteria searchCriteria) {
        searchCriteria.setMatcher("*" + searchCriteria.getMatcher() + "*");
        searchCriteria.setMatcherFields(Lists.newArrayList("email", "lastName", "firstName"));
        String roleName= (String)searchCriteria.getAdditionalFilters().get("role");
        searchCriteria.getAdditionalFilters().put("role", "(*:* !" + roleName + ")");
        searchCriteria.addEntitiesForSearch(User.class);
        return SearchPage.searchFilter(entityManager, pageQuery, searchCriteria);
    }

    @Override
    public User getCompanyAdmin(Company tenant) {
        Role adminRole = roleDao.getByRoleName(RoleEnum.COMPANY_ADMIN.getName());
        TypedQuery<User> query = entityManager.createQuery("from User where " +
                "(tenant = :tenant and role = :role)", User.class);
        query.setParameter("tenant", tenant);
        query.setParameter("role", adminRole);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<User> getByTenant(Company company) {
        String sql = "from User where tenant = :tenant";
        final TypedQuery<User> query = entityManager.createQuery(sql, User.class);
        query.setParameter("tenant", company);
        return query.getResultList();
    }

    @Override
    public User getByActivationHash(String activationHash) {
        String sql = "from User where activationHash= :activationHash";
        final TypedQuery<User> query = entityManager.createQuery(sql, User.class);
        query.setParameter("activationHash", activationHash);
        return query.getSingleResult();
    }

}

