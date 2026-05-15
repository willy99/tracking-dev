package com.tmw.tracking.domain.flex.dao.impl;

import com.google.inject.Singleton;
import com.tmw.tracking.Transaction;
import com.tmw.tracking.domain.flex.dao.FlexWarehouseDao;
import com.tmw.tracking.domain.flex.entities.FlexWarehouse;
import com.tmw.tracking.utils.DomainUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class FlexWarehouseDaoImpl implements FlexWarehouseDao {

    private static final Logger logger = LoggerFactory.getLogger(FlexWarehouseDaoImpl.class);
    private EntityManager entityManager;

    private FlexWarehouse baseWarehouse;
    private FlexWarehouse writeOffWarehouse;
    private FlexWarehouse reserveWarehouse;

    @Inject
    public FlexWarehouseDaoImpl(
            final EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    @Transaction
    @Override
    public FlexWarehouse add(FlexWarehouse warehouse) {
        entityManager.persist(warehouse);
        return warehouse;
    }

    @Transaction
    @Override
    public FlexWarehouse update(FlexWarehouse warehouse) {
        return entityManager.merge(warehouse);
    }

    @Override
    public List<FlexWarehouse> getAllWarehouses() {
        TypedQuery<FlexWarehouse> query = entityManager.createQuery("from FlexWarehouse where tenant = :tenant ", FlexWarehouse.class);
        query.setParameter("tenant", DomainUtils.getCurrentUser().getTenant());
        try {
            return query.getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public FlexWarehouse getWarehouseByName(String name) {
        TypedQuery<FlexWarehouse> query = entityManager.createQuery("from FlexWarehouse where name = :name and tenant = :tenant", FlexWarehouse.class);
        query.setParameter("name", name);
        query.setParameter("tenant", DomainUtils.getCurrentUser().getTenant());
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public FlexWarehouse getBaseWarehouse() {
        if (baseWarehouse == null) {
            baseWarehouse = getWarehouseByName(BASE_WAREHOUSE);
        }
        return baseWarehouse;
    }

    public FlexWarehouse getWriteOffWarehouse() {
        if (writeOffWarehouse == null) {
            writeOffWarehouse = getWarehouseByName(WRITE_OFF_WAREHOUSE);
        }
        return writeOffWarehouse;
    }

    public FlexWarehouse getReserveWarehouse() {
        if (reserveWarehouse == null) {
            reserveWarehouse = getWarehouseByName(RESERVE_WAREHOUSE);
        }
        return reserveWarehouse;
    }
}
