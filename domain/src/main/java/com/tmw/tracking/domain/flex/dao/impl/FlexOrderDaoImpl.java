package com.tmw.tracking.domain.flex.dao.impl;

import com.google.inject.Singleton;
import com.tmw.tracking.Transaction;
import com.tmw.tracking.domain.flex.dao.FlexDao;
import com.tmw.tracking.domain.flex.dao.FlexOrderDao;
import com.tmw.tracking.domain.flex.dao.FlexWarehouseDao;
import com.tmw.tracking.domain.flex.entities.FlexOrder;
import com.tmw.tracking.domain.flex.entities.FlexOrderTypeEnum;
import com.tmw.tracking.domain.flex.entities.FlexStatusEnum;
import com.tmw.tracking.domain.flex.entities.FlexWarehouse;
import com.tmw.tracking.domain.flex.to.FlexOrderTO;
import com.tmw.tracking.domain.flex.to.FlexTO;
import com.tmw.tracking.utils.DomainUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.*;

@Singleton
public class FlexOrderDaoImpl implements FlexOrderDao {

    private static final Logger logger = LoggerFactory.getLogger(FlexOrderDaoImpl.class);
    private EntityManager entityManager;
    private FlexWarehouseDao flexWarehouseDao;
    private FlexDao flexDao;

    @Inject
    public FlexOrderDaoImpl(
            final EntityManager entityManager,
            final FlexWarehouseDao flexWarehouseDao,
            final FlexDao flexDao) {
        this.entityManager = entityManager;
        this.flexWarehouseDao = flexWarehouseDao;
        this.flexDao = flexDao;
    }


    @Transaction
    @Override
    public FlexOrder add(FlexOrder order) {
        entityManager.persist(order);
        return order;
    }


    @Transaction
    @Override
    public void upsertOrders(List<FlexOrder> orders) {
        Map<String, FlexOrder> orderMap = new HashMap<>();
        for (FlexOrder order: orders) {
            orderMap.put(order.getOrderNumber(), order);
        }

        List<FlexOrder> existingOrders = getOrdersByNumbers(new ArrayList<String>(orderMap.keySet()));
        Map<Long, FlexOrder> ordersToRecalculate = new HashMap<>();

        Map<Long, Integer> exportedFlexForOrders = new HashMap<>();
        Map<Long, Integer> mountedFlexForOrders = new HashMap<>();

        for (FlexOrder existingOrder: existingOrders) {
            FlexOrder newOrder = orderMap.get(existingOrder.getOrderNumber());
            existingOrder.setExecutionDate(newOrder.getExecutionDate());
            if (existingOrder.getOrderType() != FlexOrderTypeEnum.IMPORT && (existingOrder.getExportContainerQty() == null ||
                    existingOrder.getExportContainerQty().equals(0) ||
                    existingOrder.getExportFlexQty() == null ||
                    existingOrder.getExportFlexQty().equals(0) ||
                    !existingOrder.getExportContainerQty().equals(newOrder.getExportContainerQty()))) {
                if (existingOrder.getExportContainerQty() == null || existingOrder.getExportContainerQty().equals(0)) {
                    existingOrder.setExportContainerQty(newOrder.getExportContainerQty());
                }
                if (existingOrder.getExportFlexQty() == null || existingOrder.getExportFlexQty().equals(0)) {
                    existingOrder.setExportFlexQty(newOrder.getExportFlexQty());
                }
                ordersToRecalculate.put(existingOrder.getId(), existingOrder);
                exportedFlexForOrders.put(existingOrder.getId(), 0);
                mountedFlexForOrders.put(existingOrder.getId(), 0);
            }
            existingOrder.setExportContainerQty(newOrder.getExportContainerQty());
            entityManager.merge(existingOrder);

        }
        for (FlexOrder order: orders) {
           if (!existingOrders.contains(order)) {
               order.setStatus(FlexStatusEnum.NEW);
               entityManager.persist(order);
           }
        }
        if (ordersToRecalculate.isEmpty()) {
            return;
        }
        //recalculate orders

        List<FlexTO> flexList = flexDao.getFlexesForOrders(new ArrayList<>(ordersToRecalculate.values()));
        //count existing
        for (FlexTO flex: flexList) {
            if (flex.getExportOrder() != null && flex.getExportOrder().getId() != null) {
                Integer count = exportedFlexForOrders.get(flex.getExportOrder().getId());
                exportedFlexForOrders.put(flex.getExportOrder().getId(), count + 1);
            }
            if (flex.getMountContainer() != null && flex.getMountContainer().getId() != null) {
                Integer count = mountedFlexForOrders.get(flex.getExportOrder().getId());
                mountedFlexForOrders.put(flex.getExportOrder().getId(), count + 1);
            }
        }
        //set corresponding statuses
        for (FlexOrder orderToRecalculate: ordersToRecalculate.values()) {
            Integer flexesAttachedToContainer = mountedFlexForOrders.get(orderToRecalculate.getId());
            Integer flexesAttachedToOrder = exportedFlexForOrders.get(orderToRecalculate.getId());

            Integer expectedFlexCount = orderToRecalculate.getExportFlexQty();
            if (expectedFlexCount == null) {
                expectedFlexCount = 0;
                System.out.println("Expected count is NULL " + orderToRecalculate);
            }

            //completed
            if (expectedFlexCount.equals(flexesAttachedToOrder)) {
                orderToRecalculate.setOrderType(FlexOrderTypeEnum.MOUNT);
                orderToRecalculate.setStatus(FlexStatusEnum.NEW);
            }
            if (expectedFlexCount.equals(flexesAttachedToContainer)) {
                orderToRecalculate.setOrderType(FlexOrderTypeEnum.MOUNT);
                orderToRecalculate.setStatus(FlexStatusEnum.COMPLETED);
            }

            //in progress
            System.out.println("---- expectedCount " + expectedFlexCount);
            System.out.println("---- flexesAttachedToContainer " + flexesAttachedToContainer);

            if (expectedFlexCount > flexesAttachedToContainer && flexesAttachedToContainer > 0) {
                orderToRecalculate.setOrderType(FlexOrderTypeEnum.MOUNT);
                orderToRecalculate.setStatus(FlexStatusEnum.IN_PROGRESS);
            }
            if (expectedFlexCount > flexesAttachedToOrder && flexesAttachedToContainer == 0) {
                orderToRecalculate.setOrderType(FlexOrderTypeEnum.EXPORT);
                orderToRecalculate.setStatus(FlexStatusEnum.IN_PROGRESS);
            }
            if (flexesAttachedToOrder == 0 && flexesAttachedToContainer == 0) {
                orderToRecalculate.setOrderType(FlexOrderTypeEnum.EXPORT);
                orderToRecalculate.setStatus(FlexStatusEnum.NEW);
            }

            if (expectedFlexCount < flexesAttachedToContainer || expectedFlexCount < flexesAttachedToOrder) {
                orderToRecalculate.setExportContainerQty(Math.max(flexesAttachedToContainer, flexesAttachedToOrder));
                orderToRecalculate.setStatus(FlexStatusEnum.IN_PROGRESS);
            }
            System.out.println(">>> order " + orderToRecalculate.getOrderNumber() + " status" + orderToRecalculate.getStatus() + " type: " + orderToRecalculate.getOrderType());
            //TODO entityManager.merge(orderToRecalculate);
        }
    }

    @Transaction
    @Override
    public FlexOrder update(FlexOrder order) {
        return entityManager.merge(order);
    }

    @Transaction
    @Override
    public void deleteOrder(FlexOrder order) {
        entityManager.remove(order);
    }

    @Transaction
    @Override
    public void deleteOrderBatch(List<FlexOrder> orderList) {
        for (FlexOrder order: orderList) {
            entityManager.remove(order);
        }
    }

    @Override
    public List<FlexOrder> getOrdersForExport(String searchString) {
        return getOrdersForType(searchString, FlexOrderTypeEnum.EXPORT);
    }

    @Override
    public List<FlexOrder> getOrdersForMount(String searchString) {
        return getOrdersForType(searchString, FlexOrderTypeEnum.MOUNT);
    }

    @Override
    public List<FlexOrder> getOrdersForImport(String searchString) {
        return getOrdersForType(searchString, FlexOrderTypeEnum.IMPORT);
    }

    @Override
    public List<FlexOrderTO> getExportOrdersWithStatistic(String searchString, Date lastUpdated) {
        String searchClause = (searchString != null && !searchString.isEmpty())? " and o.orderNumber = :searchString": "";
        String lastUpdatedClause = (lastUpdated != null)? " and (o.status != :completedStatus or (o.lastUpdated > :lastUpdated and o.status = :completedStatus)) ": "";

        String hqlQuery = "SELECT o.orderNumber, o.exportContainerQty, o.orderType, o.status, o.lastUpdated, count(f) from FlexOrder o left join o.exportFlexes f " +
                " where (f is null or (f.deleted = false and f.warehouse in (:flexWarehoses))) and o.orderType in (:orderTypes) and o.status != :orderStatus and o.tenant = :tenant " + lastUpdatedClause + searchClause + "" +
                " group by o.orderNumber, o.exportContainerQty, o.status, o.lastUpdated " +
                " order by o.orderNumber asc ";
        Query query = entityManager.createQuery(hqlQuery);
        query.setParameter("tenant", DomainUtils.getCurrentUser().getTenant());
        List<FlexOrderTypeEnum> orderTypes = new ArrayList<>();
        orderTypes.add(FlexOrderTypeEnum.EXPORT);
        orderTypes.add(FlexOrderTypeEnum.MOUNT);
        query.setParameter("orderTypes", orderTypes);
        query.setParameter("orderStatus", FlexStatusEnum.CANCELLED);
        List<FlexWarehouse> flexWarehouses = new ArrayList<>();
        flexWarehouses.add(flexWarehouseDao.getBaseWarehouse());
        flexWarehouses.add(flexWarehouseDao.getReserveWarehouse());
        query.setParameter("flexWarehoses", flexWarehouses);

        if (lastUpdated != null) {
            query.setParameter("lastUpdated", lastUpdated);
            query.setParameter("completedStatus", FlexStatusEnum.COMPLETED);
        }
        if (!searchClause.isEmpty()) {
            query.setParameter("searchString", searchString.toUpperCase());
        }

        List<FlexOrderTO> flexOrderTOS = new ArrayList<>();

        List<Object[]> resultList = query.getResultList();
        for (Object[] obj: resultList) {
            FlexOrderTO orderTO = new FlexOrderTO();
            orderTO.setOrderNumber((String)obj[0]);
            orderTO.setFlexQty( (Integer)obj[1]);
            orderTO.setOrderType((FlexOrderTypeEnum) obj[2]);
            orderTO.setStatus((FlexStatusEnum)obj[3]);
            orderTO.setUpdatedDate((Date)obj[4]);
            orderTO.setProcessedFlexQty(((Long)obj[5]).intValue());
            flexOrderTOS.add(orderTO);
        }
        return flexOrderTOS;

    }

    @Override
    public List<FlexOrderTO> getMountedOrdersWithStatistic(String searchString, Date lastUpdated) {
        String searchClause = (searchString != null && !searchString.isEmpty())? " and o.orderNumber = :searchString": "";
        String lastUpdatedClause = (lastUpdated != null)? " and (o.status != :completedStatus or (o.lastUpdated > :lastUpdated and o.status = :completedStatus)) ": "";

        String hqlQuery = "SELECT o.orderNumber, o.exportContainerQty, o.orderType, o.status, o.lastUpdated, count(f) from FlexOrder o left join o.exportFlexes as f with f.serialNumber is not null and f.mountContainer is not null  " +
                " where (f is null or (f.deleted = false and f.warehouse in (:flexWarehoses))) and o.orderType in (:orderTypes) and o.status != :orderStatus and o.tenant = :tenant " + lastUpdatedClause + searchClause + "" +
                " group by o.orderNumber, o.exportContainerQty, o.status, o.lastUpdated" +
                " order by o.orderNumber asc ";
        Query query = entityManager.createQuery(hqlQuery);
        query.setParameter("tenant", DomainUtils.getCurrentUser().getTenant());
        List<FlexOrderTypeEnum> orderTypes = new ArrayList<>();
        orderTypes.add(FlexOrderTypeEnum.EXPORT);
        orderTypes.add(FlexOrderTypeEnum.MOUNT);
        query.setParameter("orderTypes", orderTypes);
        query.setParameter("orderStatus", FlexStatusEnum.CANCELLED);
        List<FlexWarehouse> flexWarehouses = new ArrayList<>();
        flexWarehouses.add(flexWarehouseDao.getBaseWarehouse());
        flexWarehouses.add(flexWarehouseDao.getReserveWarehouse());
        query.setParameter("flexWarehoses", flexWarehouses);
        if (lastUpdated != null) {
            query.setParameter("lastUpdated", lastUpdated);
            query.setParameter("completedStatus", FlexStatusEnum.COMPLETED);
        }
        if (!searchClause.isEmpty()) {
            query.setParameter("searchString", searchString.toUpperCase());
        }

        List<FlexOrderTO> flexOrderTOS = new ArrayList<>();

        List<Object[]> resultList = query.getResultList();
        for (Object[] obj: resultList) {
            FlexOrderTO orderTO = new FlexOrderTO();
            orderTO.setOrderNumber((String)obj[0]);
            orderTO.setFlexQty( (Integer)obj[1]);
            orderTO.setOrderType((FlexOrderTypeEnum)obj[2]);
            orderTO.setStatus((FlexStatusEnum)obj[3]);
            orderTO.setUpdatedDate((Date)obj[4]);
            orderTO.setProcessedFlexQty(((Long)obj[5]).intValue());
            flexOrderTOS.add(orderTO);
        }
        return flexOrderTOS;

    }

    @Override
    public FlexOrder getOrderByNumber(String number) {
        TypedQuery<FlexOrder> query = entityManager.createQuery("from FlexOrder where orderNumber = :number and tenant = :tenant", FlexOrder.class);
        query.setParameter("tenant", DomainUtils.getCurrentUser().getTenant());
        query.setParameter("number", number);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<FlexOrder> getOrdersByNumbers(List<String> numbers) {
        if (numbers.isEmpty()) {
            return new ArrayList<>();
        }
        TypedQuery<FlexOrder> query = entityManager.createQuery("from FlexOrder where orderNumber in :numbers and tenant = :tenant", FlexOrder.class);
        query.setParameter("tenant", DomainUtils.getCurrentUser().getTenant());
        query.setParameter("numbers", numbers);
        return query.getResultList();
    }



    private List<FlexOrder> getOrdersForType(String searchString, FlexOrderTypeEnum orderType) {
        String searchClause = (searchString != null && !searchString.isEmpty())? " and orderNumber = :searchString": "";
        TypedQuery<FlexOrder> query = entityManager.createQuery("from FlexOrder where orderType = :orderType and tenant = :tenant" + searchClause + " order by orderNumber asc ", FlexOrder.class);
        query.setParameter("tenant", DomainUtils.getCurrentUser().getTenant());
        if (!searchClause.isEmpty()) {
            query.setParameter("searchString", searchString.toUpperCase());
        }
        query.setParameter("orderType", orderType);
        return query.getResultList();
    }


}
