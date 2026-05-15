package com.tmw.tracking.dao.impl;

import com.tmw.tracking.dao.ConfigurationDao;
import com.tmw.tracking.entity.ConfigurationEntity;
import com.tmw.tracking.utils.DomainUtils;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class ConfiguarationDaoImpl implements ConfigurationDao {

    private EntityManager entityManager;

    @Inject
    public ConfiguarationDaoImpl(
            final EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<ConfigurationEntity> getAllTenantConfiguraton() {
        TypedQuery<ConfigurationEntity> query = entityManager.createQuery("from ConfigurationEntity where tenant = :tenant", ConfigurationEntity.class);
        query.setParameter("tenant", DomainUtils.getCurrentUser().getTenant());
        return query.getResultList();
    }
}
