package com.tmw.tracking.dao.impl;

import com.google.inject.Singleton;
import com.tmw.tracking.Transaction;
import com.tmw.tracking.dao.AuthenticatedUserDao;
import com.tmw.tracking.dao.UserDao;
import com.tmw.tracking.entity.AuthenticatedUser;
import com.tmw.tracking.entity.Company;
import com.tmw.tracking.entity.User;
import org.apache.commons.lang.time.DateUtils;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Singleton
public class AuthenticatedUserDaoImpl implements AuthenticatedUserDao{
    private EntityManager entityManager;
    private UserDao userDao;
    @Inject
    public AuthenticatedUserDaoImpl(
            final EntityManager entityManager,
            final UserDao userDao){
        this.entityManager = entityManager;
        this.userDao = userDao;
    }

    /**
     * {@inheritDoc}
     * @see AuthenticatedUserDao#getAuthenticatedUser(User)
     */
    @Override
    public AuthenticatedUser getAuthenticatedUser(final User user){
        final Query query = entityManager.createQuery("from AuthenticatedUser where user.id = :userId and expired > :expired ");
        query.setParameter("userId", user.getId());
        query.setParameter("expired", new Date());
        try {
            return (AuthenticatedUser)query.getSingleResult();
        } catch (NoResultException e){
            return null;
        }
    }

    @Transaction
    @Override
    public AuthenticatedUser transactionallyAuthenticateUser(final User user, Integer tokenExpirationMinutes) {

        AuthenticatedUser authenticatedUser = null;
        final Query query = entityManager.createQuery("from AuthenticatedUser where user.id = :userId and expired > :expired ");
        query.setParameter("userId", user.getId());
        query.setParameter("expired", new Date());
        List<AuthenticatedUser> authenticatedUserList = query.getResultList();
        if (authenticatedUserList.isEmpty()) {
            authenticatedUser = new AuthenticatedUser();
            authenticatedUser.setUser(user);
        } else {
            authenticatedUser = authenticatedUserList.get(0);
        }

        final String token = UUID.randomUUID().toString();
        authenticatedUser.setToken(token);
        final Date expired = DateUtils.addMinutes(new Date(), tokenExpirationMinutes);
        authenticatedUser.setExpired(expired);
        if (authenticatedUser.getId() == null) {
            create(authenticatedUser);
        }
        else {
            update(authenticatedUser);
        }
        return authenticatedUser;

    }

    /**
     * {@inheritDoc}
     * @see AuthenticatedUserDao#getAuthenticatedUser(User)
     */
    @Override
    public AuthenticatedUser getAuthenticatedUserByToken(final String token) {
        final Query query = entityManager.createQuery("from AuthenticatedUser where token = :token");
        query.setParameter("token", token);
        try {
            return (AuthenticatedUser)query.getSingleResult();
        } catch (NoResultException e){
            return null;
        }
    }

    /**
     * {@inheritDoc}
     * @see AuthenticatedUserDao#create(AuthenticatedUser)
     */
    //@Transaction
    @Override
    public void create(final AuthenticatedUser authenticatedUser) {
        entityManager.persist(authenticatedUser);
    }

    /**
     * {@inheritDoc}
     * @see AuthenticatedUserDao#update(AuthenticatedUser)
     */
    //@Transaction
    @Override
    public void update(final AuthenticatedUser authenticatedUser){
        entityManager.merge(authenticatedUser);
    }

    /**
     * {@inheritDoc}
     * @see AuthenticatedUserDao#delete(AuthenticatedUser)
     */
    @Transaction
    @Override
    public void delete(final AuthenticatedUser authenticatedUser) {
        entityManager.remove(authenticatedUser);
    }

    //TODO Admin only
    @Transaction
    @Override
    public void deactivateCompany(Company company) {
        for (User user: userDao.getByTenant(company)) {
            AuthenticatedUser authUser = getAuthenticatedUser(user);
            if (authUser != null) {
                entityManager.remove(authUser);
            }
        }
    }
}
