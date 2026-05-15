package com.tmw.tracking.dao.impl;

import com.tmw.tracking.Transaction;
import com.tmw.tracking.dao.TerminalDao;
import com.tmw.tracking.entity.Terminal;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;


public class TerminalDaoImpl implements TerminalDao {

    private EntityManager entityManager;

    @Inject
    public TerminalDaoImpl(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transaction
    @Override
    public Terminal create(Terminal terminal) {
       entityManager.persist(terminal);
       return terminal;
    }

    @Override
    public Terminal getByTerminalName(String terminalName) {
        final TypedQuery<Terminal> query = entityManager.createQuery("from Terminal where name = :terminalName", Terminal.class);
        query.setParameter("terminalName", terminalName);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
