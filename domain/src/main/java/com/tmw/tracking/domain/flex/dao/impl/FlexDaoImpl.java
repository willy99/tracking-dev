package com.tmw.tracking.domain.flex.dao.impl;

import com.google.inject.Singleton;
import com.tmw.tracking.Transaction;
import com.tmw.tracking.domain.flex.dao.FlexDao;
import com.tmw.tracking.domain.flex.dao.FlexWarehouseDao;
import com.tmw.tracking.domain.flex.entities.*;
import com.tmw.tracking.domain.flex.to.FlexContainerTO;
import com.tmw.tracking.domain.flex.to.FlexOrderTO;
import com.tmw.tracking.domain.flex.to.FlexTO;
import com.tmw.tracking.domain.flex.to.FlexWarehouseTO;
import com.tmw.tracking.utils.DomainUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Singleton
public class FlexDaoImpl implements FlexDao {

    private static final Logger logger = LoggerFactory.getLogger(FlexDaoImpl.class);
    private EntityManager entityManager;
    private FlexWarehouseDao flexWarehouseDao;

    @Inject
    public FlexDaoImpl(
            final EntityManager entityManager,
            final FlexWarehouseDao flexWarehouseDao) {
        this.entityManager = entityManager;
        this.flexWarehouseDao = flexWarehouseDao;
    }

    @Transaction
    @Override
    public Flex add(Flex flex) {
        entityManager.persist(flex);
        return flex;
    }

    @Transaction
    @Override
    public void updateBatch(List<Flex> flexList, List<Flex> toUpdate) {
        for (Flex flex: flexList) {
            entityManager.persist(flex);
        }
        for (Flex flex: flexList) {
            entityManager.merge(flex);
        }
    }

    @Transaction
    @Override
    public Flex update(Flex flex) {
        return entityManager.merge(flex);
    }

    @Override
    public Flex getBySerialNumber(String serialNumber) {
        TypedQuery<Flex> query = entityManager.createQuery("from Flex where serialNumber = :serialNumber and tenant = :tenant", Flex.class);
        query.setParameter("serialNumber", serialNumber);
        query.setParameter("tenant", DomainUtils.getCurrentUser().getTenant());
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<Flex> getAllByWarehouse(String warehouseName) {
        TypedQuery<Flex> query = entityManager.createQuery("from Flex where deleted = false and warehouse.name = :warehouseName and tenant = :tenant", Flex.class);
        query.setParameter("tenant", DomainUtils.getCurrentUser().getTenant());
        query.setParameter("warehouseName", warehouseName);
        try {
            return query.getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public List<FlexTO> getFlexTOListByWarehouse(String warehouseName) {
        String hqlQuery = "SELECT f.id, f.serialNumber, f.importDate, f.exportDate, f.writeOffDate, f.mountDate, w.id, w.name, c.containerNumber " +
                " from Flex f left join f.warehouse w " +
                " left join f.importContainer c " +
                " where f.deleted = false and f.warehouse.name = :warehouseName and f.tenant = :tenant " ;
        Query query = entityManager.createQuery(hqlQuery);
        query.setParameter("tenant", DomainUtils.getCurrentUser().getTenant());
        query.setParameter("warehouseName", warehouseName);
        List<Object[]> resultList = query.getResultList();
        List<FlexTO> flexToList = new ArrayList<>();
        for (Object[] obj: resultList) {
            flexToList.add(createFlexTOFromWarehouseObject(obj));
        }
        return flexToList;
    }

    @Override
    public Integer getImportedFlexCountForContainer(FlexContainer container) {
        return DomainUtils.getRowCount(entityManager, Flex.class, " where tenant = " + DomainUtils.getCurrentUser().getTenant().getId() + " and importContainer.id = " + container.getId());
    }

    @Override
    public Integer getExportedFlexCountForOrder(FlexOrder order) {
        return DomainUtils.getRowCount(
                entityManager,
                Flex.class,
                " where tenant = " + DomainUtils.getCurrentUser().getTenant().getId() +
                        " and exportOrder.id = " + order.getId() +
                        " and warehouse.id = " + flexWarehouseDao.getBaseWarehouse().getId());
    }

    @Override
    public Integer getMountedFlexCountForOrder(FlexOrder order) {
        return DomainUtils.getRowCount(
                entityManager,
                Flex.class,
                " where tenant = " + DomainUtils.getCurrentUser().getTenant().getId() +
                        " and mountContainer != null and exportOrder.id = " + order.getId() +
                        " and warehouse.id = " + flexWarehouseDao.getBaseWarehouse().getId());
    }

    @Override
    public List<Flex> getFlexListForImportedOrders(Date lastUpdated, FlexStatusEnum orderStatus) {
        String statusClause = orderStatus != null? " importContainer.importOrder.status = :status and " : "";
        TypedQuery<Flex> query = entityManager.createQuery("from Flex where " + statusClause + " importContainer.importOrder.lastUpdated > :lastUpdated and deleted = false and tenant = :tenant", Flex.class);
        query.setParameter("tenant", DomainUtils.getCurrentUser().getTenant());
        if (orderStatus != null) {
            query.setParameter("status", orderStatus);
        }
        query.setParameter("lastUpdated", lastUpdated);
        return query.getResultList();
    }

    @Override
    public List<Flex> getFlexListForMountedOrders(Date lastUpdated) {
        TypedQuery<Flex> query = entityManager.createQuery("from Flex where exportOrder.status = :status and exportOrder.orderType = :orderType and exportOrder.lastUpdated > :lastUpdated and deleted = false and tenant = :tenant order by exportOrder.orderNumber asc ", Flex.class);
        query.setParameter("tenant", DomainUtils.getCurrentUser().getTenant());
        query.setParameter("status", FlexStatusEnum.COMPLETED);
        query.setParameter("orderType", FlexOrderTypeEnum.MOUNT);
        query.setParameter("lastUpdated", lastUpdated);
        return query.getResultList();
    }

    @Override
    public List<FlexRename> getRenamedFlexList() {
        TypedQuery<FlexRename> query = entityManager.createQuery("from FlexRename where tenant = :tenant order by lastUpdated asc", FlexRename.class);
        query.setParameter("tenant", DomainUtils.getCurrentUser().getTenant());
        return query.getResultList();
    }

    @Override
    public List<String> getRemovedFlexList() {
        TypedQuery<Flex> query = entityManager.createQuery("from Flex where deleted = true and tenant = :tenant order by lastUpdated asc", Flex.class);
        query.setParameter("tenant", DomainUtils.getCurrentUser().getTenant());
        List<Flex> flexList = query.getResultList();

        List<String> serialNumList = new ArrayList<>();
        for (Flex flex: flexList) {
            serialNumList.add(flex.getSerialNumber());
        }
        return serialNumList;
    }

    @Override
    public void updateFlexRename(FlexRename flexRename) {
        entityManager.persist(flexRename);
    }

    @Override
    public List<Flex> getBySerialNumbers(List<String> serialNumbers) {
        if (serialNumbers.isEmpty()) {
            return new ArrayList<>();
        }
        TypedQuery<Flex> query = entityManager.createQuery("from Flex where serialNumber in :serialNumbers and tenant = :tenant", Flex.class);
        query.setParameter("tenant", DomainUtils.getCurrentUser().getTenant());
        query.setParameter("serialNumbers", serialNumbers);
        return query.getResultList();
    }

    @Override
    public List<Flex> getFlexListForExportOrderByNums(List<String> flexOrderNums) {
        TypedQuery<Flex> query = entityManager.createQuery("from Flex f where f.exportOrder.orderNumber in :orderNums and tenant = :tenant", Flex.class);
        query.setParameter("tenant", DomainUtils.getCurrentUser().getTenant());
        query.setParameter("orderNums", flexOrderNums);
        return query.getResultList();
    }

    @Override
    public List<FlexTO> getImportedFlexes(Date lastUpdated, String containerNum) {
        String containerClause = containerNum != null? " and f.importContainer.containerNumber = :containerNum ": "";
        String lastUpdatedClause = lastUpdated != null? " and o.lastUpdated > :lastUpdated ": "";
        String hqlQuery = "SELECT f.id, f.serialNumber, w.id, w.name, f.exportOrder.id, f.mountContainer.id from Flex f " +
                " left join f.warehouse w " +
                " left join f.importContainer c left join c.importOrder o " +
                " where f.deleted = false and c.importOrder is not null and c.tenant = :tenant " + lastUpdatedClause + containerClause + " order by f.serialNumber asc ";
        Query query = entityManager.createQuery(hqlQuery);
        query.setParameter("tenant", DomainUtils.getCurrentUser().getTenant());
        if (lastUpdated != null) {
            query.setParameter("lastUpdated", lastUpdated);
        }
        if (containerNum != null) {
            query.setParameter("containerNum", containerNum);
        }
        List<Object[]> resultList = query.getResultList();
        List<FlexTO> flexToList = new ArrayList<>();
        for (Object[] obj: resultList) {
            flexToList.add(createFlexTOFromObject(obj));
        }

        return flexToList;

    }

    @Override
    public List<FlexTO> getExportFlexes(Date lastUpdated, String orderNum) {
        String orderClause = orderNum != null? " and o.orderNumber = :orderNum ": "";
        String hqlQuery = "SELECT f.id, f.serialNumber, w.id, w.name, o.id, o.orderNumber, o.exportContainerQty, c.id, c.containerNumber, ci.containerNumber from Flex f " +
                " left join f.warehouse w " +
                " left join f.exportOrder o " +
                " left join f.mountContainer c " +
                " left join f.importContainer ci " +
                " where f.deleted = false and f.exportOrder is not null and f.tenant = :tenant and o.lastUpdated > :lastUpdated " +orderClause + " order by f.serialNumber ";
        Query query = entityManager.createQuery(hqlQuery);
        query.setParameter("tenant", DomainUtils.getCurrentUser().getTenant());
        query.setParameter("lastUpdated", lastUpdated);
        if (orderNum != null) {
            query.setParameter("orderNum", orderNum);
        }
        List<Object[]> resultList = query.getResultList();
        List<FlexTO> flexToList = new ArrayList<>();
        for (Object[] obj: resultList) {
            flexToList.add(createMountedFlexTOFromObject(obj));
        }

        return flexToList;

    }

    @Override
    public List<FlexTO> getMountedFlexes(Date lastUpdated, String orderNum) {
        String orderClause = orderNum != null? " and o.orderNumber = :orderNum ": "";
        String hqlQuery = "SELECT f.id, f.serialNumber, w.id, w.name, o.id, o.orderNumber, o.exportContainerQty, c.id, c.containerNumber, ci.containerNumber from Flex f " +
                " left join f.warehouse w left join f.exportOrder o " +
                " left join f.mountContainer c " +
                " left join f.importContainer ci " +
                " where f.deleted = false and f.exportOrder is not null and f.mountContainer is not null and f.tenant = :tenant and o.lastUpdated > :lastUpdated " +orderClause + " order by f.serialNumber ";
        Query query = entityManager.createQuery(hqlQuery);
        query.setParameter("tenant", DomainUtils.getCurrentUser().getTenant());
        query.setParameter("lastUpdated", lastUpdated);
        if (orderNum != null) {
            query.setParameter("orderNum", orderNum);
        }
        List<Object[]> resultList = query.getResultList();
        List<FlexTO> flexToList = new ArrayList<>();
        for (Object[] obj: resultList) {
            flexToList.add(createMountedFlexTOFromObject(obj));
        }

        return flexToList;

    }


    @Override
    public List<FlexTO> getFlexesForOrders(List<FlexOrder> orders) {
        String hqlQuery = "SELECT f.id, f.serialNumber, w.id, w.name, o.id, o.orderNumber, o.exportContainerQty, c.id, c.containerNumber, ci.containerNumber from Flex f " +
                " left join f.warehouse w " +
                " left join f.exportOrder o " +
                " left join f.mountContainer c " +
                " left join f.importContainer ci " +
                " where f.deleted = false and f.exportOrder in :orders and f.tenant = :tenant" +
                " order by f.serialNumber ";
        Query query = entityManager.createQuery(hqlQuery);
        query.setParameter("tenant", DomainUtils.getCurrentUser().getTenant());
        query.setParameter("orders", orders);
        List<Object[]> resultList = query.getResultList();
        List<FlexTO> flexToList = new ArrayList<>();
        for (Object[] obj: resultList) {
            flexToList.add(createMountedFlexTOFromObject(obj));
        }

        return flexToList;

    }


    private List<FlexTO> getFlexesFromWareHouse(FlexWarehouse flexWarehouse) {
        String hqlQuery = "SELECT f.id, f.serialNumber, w.id, w.name, f.exportOrder.id, f.mountContainer.id  from Flex f " +
                " left join f.warehouse w " +
                " where f.deleted = false and f.warehouse = :warehouse and f.tenant = :tenant order by f.serialNumber " ;
        Query query = entityManager.createQuery(hqlQuery);
        query.setParameter("tenant", DomainUtils.getCurrentUser().getTenant());
        query.setParameter("warehouse", flexWarehouse);
        List<Object[]> resultList = query.getResultList();
        List<FlexTO> flexToList = new ArrayList<>();
        for (Object[] obj: resultList) {
            flexToList.add(createFlexTOFromObject(obj));
        }

        return flexToList;

    }

    @Override
    public List<FlexTO> getReservedFlexes() {
        List<FlexTO> flexes = getFlexesFromWareHouse(flexWarehouseDao.getReserveWarehouse());
        //filter out those which already gone
        List<FlexTO> result = new ArrayList<>();
        for (FlexTO flexTO: flexes) {
            if (flexTO.getMountContainer() == null) {
                result.add(flexTO);
            }
        }
        return result;
    }

    @Override
    public List<FlexTO> getBaseFlexes() {
        return getFlexesFromWareHouse(flexWarehouseDao.getBaseWarehouse());
    }

    @Transaction
    @Override
    public void removeFlex(Flex flex) {
        flex.setDeleted(true);
        flex.setWarehouse(null);
        flex.setImportContainer(null);
        flex.setExportOrder(null);
        entityManager.merge(flex);
    }

    @Transaction
    @Override
    public void removeFlexBatch(Set<Flex> flexesToRemove) {
        for (Flex flex: flexesToRemove) {
            flex.setDeleted(true);
            flex.setWarehouse(null);
            flex.setImportContainer(null);
            flex.setExportOrder(null);
            entityManager.merge(flex);
        }
    }

    private FlexTO createFlexTOFromObject(Object[] obj) {
        FlexTO flexTO = new FlexTO();
        FlexWarehouseTO warehouseTO = new FlexWarehouseTO();
        flexTO.setId((Long)obj[0]);
        flexTO.setSerialNumber((String)obj[1]);
        warehouseTO.setId((Long)obj[2]);
        warehouseTO.setName((String)obj[3]);

        flexTO.setWarehouse(warehouseTO);

        if (obj[4] != null) {
            FlexOrderTO exportOrderTO = new FlexOrderTO();
            exportOrderTO.setId( ((Long) obj[4]));
            flexTO.setExportOrder(exportOrderTO);
        }
        if (obj[5] != null) {
            FlexContainerTO mountContainerTO = new FlexContainerTO();
            mountContainerTO.setId(((Long)obj[5]));
            flexTO.setMountContainer(mountContainerTO);

        }

        return flexTO;
    }

    private FlexTO createMountedFlexTOFromObject(Object[] obj) {
        FlexTO flexTO = new FlexTO();
        FlexWarehouseTO warehouseTO = new FlexWarehouseTO();
        warehouseTO.setName((String)obj[3]);
        warehouseTO.setId((Long)obj[2]);

        flexTO.setWarehouse(warehouseTO);
        flexTO.setId((Long)obj[0]);
        flexTO.setSerialNumber((String)obj[1]);
        FlexOrderTO flexOrderTO = new FlexOrderTO();
        flexOrderTO.setId((Long)obj[4]);
        flexOrderTO.setOrderNumber((String)obj[5]);
        flexOrderTO.setFlexQty((Integer)obj[6]);
        flexTO.setExportOrder(flexOrderTO);

        FlexContainerTO containerTO = new FlexContainerTO();
        containerTO.setId((Long)obj[7]);
        containerTO.setContainerNumber((String)obj[8]);
        flexTO.setMountContainer(containerTO);
        flexTO.setMountContainerNumber(containerTO.getContainerNumber());

        FlexContainerTO importContainerTO = new FlexContainerTO();
        importContainerTO.setContainerNumber((String)obj[9]);
        flexTO.setImportContainer(importContainerTO);
        flexTO.setImportContainerNumber(importContainerTO.getContainerNumber());

        return flexTO;
    }


    //        String hqlQuery = "SELECT f.id, f.serialNumber, f.importDate, f.exportDate, f.writeOffDate, f.mountDate, w.id, w.name, c.containerNumber " +
    private FlexTO createFlexTOFromWarehouseObject(Object[] obj) {
        FlexTO flexTO = new FlexTO();
        FlexWarehouseTO warehouseTO = new FlexWarehouseTO();
        warehouseTO.setName((String)obj[7]);
        warehouseTO.setId((Long)obj[6]);

        flexTO.setWarehouse(warehouseTO);
        flexTO.setId((Long)obj[0]);
        flexTO.setSerialNumber((String)obj[1]);
        flexTO.setImportDate((Date)obj[2]);
        flexTO.setExportDate((Date)obj[3]);
        flexTO.setWriteOffDate((Date)obj[4]);
        flexTO.setMountDate((Date)obj[5]);

        FlexContainerTO containerTO = new FlexContainerTO();
        containerTO.setContainerNumber((String)obj[8]);
        flexTO.setImportContainer(containerTO);
        flexTO.setImportContainerNumber(containerTO.getContainerNumber());
        return flexTO;
    }


}
