package com.tmw.tracking.dao.impl;

import com.google.inject.Singleton;
import com.tmw.tracking.Transaction;
import com.tmw.tracking.dao.RoleDao;
import com.tmw.tracking.domain.SearchCriteria;
import com.tmw.tracking.entity.Role;
import com.tmw.tracking.entity.paging.Page;
import com.tmw.tracking.utils.DomainUtils;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class RoleDaoImpl implements RoleDao {

    //private final static Logger logger = LoggerFactory.getLogger(RoleDao.class);

    private EntityManager entityManager;


    @Inject
    public RoleDaoImpl(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * {@inheritDoc}
     *
     * @see RoleDao#getById(Long)
     */
    @Override
    public Role getById(final Long id) {
        final TypedQuery<Role> query = entityManager.createQuery("from Role where id = :id", Role.class);
        query.setParameter("id", id);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Role getByRoleName(String roleName) {
        final TypedQuery<Role> query = entityManager.createQuery("from Role where roleName = :roleName", Role.class);
        query.setParameter("roleName", roleName);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Transaction
    @Override
    public Role update(Role role) {
        if (role.getId() == null) {
            entityManager.persist(role);
        } else {
            entityManager.merge(role);
        }
        return role;
    }

    @Transaction
    @Override
    public void delete(Role role) {
        entityManager.remove(role);
    }

    private String getQueryForWeb(SearchCriteria searchCriteria) {

        return

                " select r.id, r.role_name, r.editable, COALESCE(u.cnt,0) as users, coalesce(p.cnt, 0) as permissions " +
                " from tr_role r " +
                " left join ( " +
                "   select role, count(*) as cnt from tr_user group by role) u " +
                "   ON r.id = u.role " +
                " left join ( " +
                "   select role_id, count(*) as cnt from tr_role_tr_permission group by role_id) p " +
                "   ON r.id = p.role_id " +
                (!searchCriteria.getMatcher().isEmpty()?" where r.role_name = :name ":" ") +
                " order by r.role_name ";


        /*return "select r.id, r.role_name, r.editable, count(u.id)" +
                " from tr_role r " +
                " left join tr_user u on u.role = r.id " +
                (!searchCriteria.getMatcher().isEmpty()?" where r.role_name = :name ":" ") +
                " group by r.id, r.role_name, r.editable " +
                " order by r.role_name ";*/
    }

    @SuppressWarnings("unchecked")
    private List<Role> createRoles(final Query query) {
        List<Role> roleList = new ArrayList<Role>();
        List<Object[]> resultList = query.getResultList();

        for (Object[] row : resultList) {
            Role role = new Role();
            role.setId(((Number) row[0]).longValue());
            role.setRoleName((String) row[1]);
            role.setEditable((Character) row[2] == 'Y');
            role.setUsers(((Number) row[3]).intValue());
            role.setCheckedPermissions(((Number) row[4]).intValue());
            roleList.add(role);
        }

        return roleList;
    }

    @Override
    public Page<Role> getForWeb(SearchCriteria searchCriteria) {
        String textQuery = getQueryForWeb(searchCriteria);
        textQuery = Page.applySorting(textQuery, "r", searchCriteria.getSortColumn(), searchCriteria.getSortOrder());

        final Query query = entityManager.createNativeQuery(textQuery);
        if (!searchCriteria.getMatcher().isEmpty()) {
            query.setParameter("name", searchCriteria.getMatcher());
        }
        query.setFirstResult(Page.getOffset(searchCriteria.getPage()));
        query.setMaxResults(Page.ITEMS_ON_PAGE);

        int count = 0;
        List<Role> roles = createRoles(query);

        if (!roles.isEmpty()) {
            count = DomainUtils.getRowCount(entityManager, Role.class);
        }

        return Page.of(roles, count, searchCriteria.getPage());
    }

    @Override
    public List<Role> getAll() {
        final TypedQuery<Role> query = entityManager.createQuery("from Role " +
                "order by roleName", Role.class);
        return query.getResultList();

    }

    @Override
    public List<Role> getAll(boolean includeSystem) {
        final TypedQuery<Role> query = entityManager.createQuery("from Role where assignable = :assignable " +
                "order by roleName", Role.class);
        query.setParameter("assignable", !includeSystem);
        return query.getResultList();
    }


}
