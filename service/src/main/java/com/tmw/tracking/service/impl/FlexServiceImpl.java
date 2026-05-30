package com.tmw.tracking.service.impl;

import com.tmw.tracking.domain.flex.dao.*;
import com.tmw.tracking.domain.flex.entities.*;
import com.tmw.tracking.domain.flex.to.*;
import com.tmw.tracking.service.ConfigurationService;
import com.tmw.tracking.service.FlexService;
import com.tmw.tracking.utils.ValidationUtils;
import com.tmw.tracking.web.controller.FlexController;
import com.tmw.tracking.web.service.exception.ValidationException;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.*;

public class FlexServiceImpl implements FlexService {

    private final static Logger logger = LoggerFactory.getLogger(FlexServiceImpl.class);
    private final FlexDao flexDao;
    private final FlexWarehouseDao flexWarehouseDao;
    private final FlexContainerDao flexContainerDao;
    private final FlexOrderDao flexOrderDao;
    private final FlexHistoryDao flexHistoryDao;

    private final ConfigurationService configurationService;

    @Inject
    public FlexServiceImpl(
            final FlexDao flexDao,
            final FlexHistoryDao flexHistoryDao,
            final FlexOrderDao flexOrderDao,
            final FlexContainerDao flexContainerDao,
            final FlexWarehouseDao flexWarehouseDao,
            final ConfigurationService configurationService
            ) {
        this.flexDao = flexDao;
        this.flexOrderDao = flexOrderDao;
        this.flexHistoryDao = flexHistoryDao;
        this.flexContainerDao = flexContainerDao;
        this.flexWarehouseDao = flexWarehouseDao;
        this.configurationService = configurationService;
    }

    @Override
    public List<FlexOrder> getAvailableOrdersForImport(String searchString) {
        return flexOrderDao.getOrdersForImport(searchString);
    }

    @Override
    public List<FlexContainerTO> getAvailableContainersForImport(String searchString) {
        return flexContainerDao.getContainersForImport(searchString, getLastUpdatedDateByConfig());
    }

    @Override
    public void importStageFrom1c(FlexImportPackage flexImportPackage) {
        //validation
        logger.debug(" >> 1c importStageFrom1c: ");
        List<FlexOrder> flexOrders = new ArrayList<>();
        List<FlexContainer> flexContainers = new ArrayList<>();
        if (flexImportPackage.orders == null) {
            throw new ValidationException("Order list is empty");
        }
        Map<String, FlexContainer> flexContainerMap = new HashMap<>();
        Set<String> containerSet = new HashSet<>();
        Set<String> addedContainerNumberSet = new HashSet<String>();

        List<String> flexOrdersToDelete = new ArrayList<>();
        for (FlexImportPackage.Order order: flexImportPackage.orders) {
            if (order.deleted) {
                flexOrdersToDelete.add(ValidationUtils.validateFlexOrderNum(order.orderNum));
                System.out.println(" >> to delete: " + order.orderNum);
            } else {
                FlexOrder flexOrder = new FlexOrder();
                flexOrder.setOrderType(FlexOrderTypeEnum.IMPORT);
                flexOrder.setOrderNumber(ValidationUtils.validateFlexOrderNum(order.orderNum));
                flexOrders.add(flexOrder);
                for (FlexImportPackage.Container container : order.containers) {
                    containerSet.add(ValidationUtils.validateFlexContainerNum(container.containerNumber));
                    System.out.println(" >> new container: " + container.containerNumber);
                }
            }
        }
        flexOrderDao.upsertOrders(flexOrders);
        flexOrders = getOrdersByNumber(FlexUtil.parseOrderNumbers(flexOrders));
        List<FlexContainer> existingContainers = flexContainerDao.getContainersByNumbers(new ArrayList<>(containerSet));
        for (FlexContainer flexContainer: existingContainers) {
            flexContainerMap.put(flexContainer.getContainerNumber(), flexContainer);
        }

        for (FlexImportPackage.Order order: flexImportPackage.orders) {
            if (order.deleted) {
                continue;
            }
            if (order.containers == null) {
                throw new ValidationException("Container list is empty");
            }
            FlexOrder flexOrder = FlexUtil.findOrderInCollection(flexOrders, order.orderNum);

            for (FlexImportPackage.Container container : order.containers) {
                FlexContainer flexContainer = flexContainerMap.get(ValidationUtils.validateFlexContainerNum(container.containerNumber));
                if (flexContainer == null) {
                    flexContainer = new FlexContainer();
                    flexContainer.setStatus(FlexStatusEnum.NEW);
                }
                flexContainer.setImportFlexQty(container.flexQty);
                flexContainer.setImportOrder(flexOrder);
                flexContainer.setContainerNumber(ValidationUtils.validateFlexContainerNum(container.containerNumber));
                if (!addedContainerNumberSet.contains(container.containerNumber)) {
                    flexContainers.add(flexContainer);
                    addedContainerNumberSet.add(flexContainer.getContainerNumber());
                }
                System.out.println(" >> new container: " + flexContainer.getContainerNumber());
            }
        }
        upsertContainers(flexContainers);

        if (!flexOrdersToDelete.isEmpty()) {
            flexOrderDao.deleteOrderBatch(flexOrderDao.getOrdersByNumbers(flexOrdersToDelete));
        }

    }

    @Override
    public List<FlexOrderTO> getMountedOrders() {
        List<Flex> flexes = flexDao.getFlexListForMountedOrders(getLastUpdatedDateByConfig());
        return mapExportedOrders(flexes);
    }

    private Date getLastUpdatedDateByConfig() {
        Integer days = configurationService.getIntegerConfigurationByName("Flex_ExpirationPeriod_Days", 30);
        return DateUtils.addDays(new Date(), -days);
    }

    @Override
    public List<FlexOrderTO> getImportedOrders() {
        List<Flex> flexes = flexDao.getFlexListForImportedOrders(getLastUpdatedDateByConfig(), FlexStatusEnum.COMPLETED);
        return mapImportedOrders(flexes);
    }

    @Override
    public void exportStageFrom1c(FlexExportPackage flexExportPackage) {
        System.out.println(" >> exportStageFrom1c");
        List<FlexOrder> flexOrdersToUpdate = new ArrayList<>();
        List<String> flexOrdersToDelete = new ArrayList<>();
        if (flexExportPackage.orders == null) {
            throw new ValidationException("Order list is empty");
        }
        List<FlexContainer> flexContainers = new ArrayList<>();
        Map<String, FlexContainer> flexContainerMap = new HashMap<>();
        Set<String> containerSet = new HashSet<>();
        Set<String> addedContainerNumberSet = new HashSet<String>();

        for (FlexExportPackage.Order order: flexExportPackage.orders) {

            System.out.println(" >> 1c package ordernum: " + order.orderNum + " exp: " + order.exportContainerQty);
            if (order.deleted) {
                flexOrdersToDelete.add(ValidationUtils.validateFlexOrderNum(order.orderNum));
            } else {
                FlexOrder flexOrder = new FlexOrder();
                flexOrder.setOrderType(FlexOrderTypeEnum.EXPORT);
                flexOrder.setOrderNumber(ValidationUtils.validateFlexOrderNum(order.orderNum));
                flexOrder.setExportContainerQty(order.exportContainerQty);

                flexOrder.setExecutionDate(order.executionDate);

                Integer expectedFlexQty = 0;
                for (FlexExportPackage.Container container : order.containers) {
                    containerSet.add(ValidationUtils.validateFlexContainerNum(container.containerNumber));
                    if (container.flexQty == null || container.flexQty == 0) {
                        expectedFlexQty += 1;
                    } else {
                        expectedFlexQty += container.flexQty;
                    }
                    System.out.println(" >> new container: " + container.containerNumber);
                }
                flexOrder.setExportFlexQty(expectedFlexQty);
                flexOrdersToUpdate.add(flexOrder);
            }
        }

        flexOrderDao.upsertOrders(flexOrdersToUpdate);
        List<FlexOrder> flexOrders = new ArrayList<>();

        flexOrders = getOrdersByNumber(FlexUtil.parseOrderNumbers(flexOrders));
        List<FlexContainer> existingContainers = flexContainerDao.getContainersByNumbers(new ArrayList<>(containerSet));
        for (FlexContainer flexContainer: existingContainers) {
            flexContainerMap.put(flexContainer.getContainerNumber(), flexContainer);
        }

        for (FlexExportPackage.Order order: flexExportPackage.orders) {
            if (order.deleted) {
                continue;
            }
            if (order.containers == null || order.containers.isEmpty()) {
                continue;
            }
            FlexOrder flexOrder = FlexUtil.findOrderInCollection(flexOrders, order.orderNum);

            for (FlexExportPackage.Container container : order.containers) {
                FlexContainer flexContainer = flexContainerMap.get(ValidationUtils.validateFlexContainerNum(container.containerNumber));
                if (flexContainer == null) {
                    flexContainer = new FlexContainer();
                    flexContainer.setStatus(FlexStatusEnum.NEW);
                }
                flexContainer.setExportFlexQty(container.flexQty);
                flexContainer.setExportOrder(flexOrder);
                flexContainer.setImportOrder(null);
                flexContainer.setContainerNumber(ValidationUtils.validateFlexContainerNum(container.containerNumber));
                if (!addedContainerNumberSet.contains(container.containerNumber)) {
                    flexContainers.add(flexContainer);
                    addedContainerNumberSet.add(flexContainer.getContainerNumber());
                }
            }
        }
        upsertContainers(flexContainers);

        //move assigned flex back from deleted orders
        if (!flexOrdersToDelete.isEmpty()) {
            List<Flex> flexListToMoveBack = flexDao.getFlexListForExportOrderByNums(flexOrdersToDelete);
            for (Flex flex: flexListToMoveBack) {
                flex.setExportOrder(null);
                flex.setMountContainer(null);
            }
            flexDao.updateBatch(new ArrayList<>(), flexListToMoveBack);
            flexOrderDao.deleteOrderBatch(flexOrderDao.getOrdersByNumbers(flexOrdersToDelete));
        }
    }

    @Override
    public List<FlexOrder> getAvailableOrdersForExport(String searchString) {
        return flexOrderDao.getOrdersForExport(searchString);
    }

    @Override
    public List<Flex> getFlexListForWarehouse(String warehouseName) {
        return flexDao.getAllByWarehouse(warehouseName);

    }

    @Override
    public List<FlexTO> getFlexListWrittenOff() {
        return flexDao.getFlexTOListByWarehouse(flexWarehouseDao.getWriteOffWarehouse().getName());
    }

    @Override
    public void upsertContainers(List<FlexContainer> containers) {
        validateFlexContainers(containers, configurationService.getBooleanConfigurationByName("Flex_SkipInvalid1cRecords", false));
        flexContainerDao.upsertAll(containers);
    }

    @Override
    public List<FlexContainer> getContainerListByOrder(String orderNum) {
         return flexContainerDao.getContainerListByOrder(orderNum);
    }

    @Override
    public FlexContainer getContainerByNumber(String containerNumber) {
        FlexContainer container = flexContainerDao.getContainerByNumber(containerNumber);
        if (container == null) {
            throw new ValidationException("Flex container " + containerNumber + " can not be found ");
        }
        return container;
    }

    @Override
    public List<FlexRenameTO> getRenamedFlexList() {
        List<FlexRenameTO> renamedFlexList = new ArrayList<>();
        List<FlexRename> flexRenameList = flexDao.getRenamedFlexList();
        for (FlexRename flexRename: flexRenameList) {
            FlexRenameTO flexRenameTO = new FlexRenameTO();
            flexRenameTO.setOldSerialNum(flexRename.getOldSerialNumber());
            flexRenameTO.setNewSerialNum(flexRename.getNewSerialNumber());
            flexRenameTO.setUpdatedDate(flexRename.getLastUpdated());
            renamedFlexList.add(flexRenameTO);
        }
        return renamedFlexList;
    }

    @Override
    public List<Flex> getBySerialNumbers(List<String> serialNumbers) {
        return flexDao.getBySerialNumbers(serialNumbers);
    }

    @Override
    public StatisticTO getStatistic() {

        List<FlexTO> flexList = flexDao.getImportedFlexes(null, null);
        StatisticTO statisticTO = new StatisticTO();

        for (FlexTO flex: flexList) {
            statisticTO.incremenTotal();
            Long warehouseId = flex.getWarehouse().getId();
            if (flex.getMountContainer() != null) {
                statisticTO.incrementExported();
            }
            if (flexWarehouseDao.getBaseWarehouse().getId().equals(warehouseId) && flex.getMountContainer() == null) {
                statisticTO.incrementBaseWarehouse();
            }
            if (flexWarehouseDao.getWriteOffWarehouse().getId().equals(warehouseId)) {
                statisticTO.incrementWrittenWarehouse();
            }
            if (flexWarehouseDao.getReserveWarehouse().getId().equals(warehouseId) && flex.getMountContainer() == null) {
                statisticTO.incrementReservedWarehouse();
            }
        }

        Integer flexQty = 0;
        List<FlexContainerTO> containerList = flexContainerDao.getContainersForImport(null, null);
        for (FlexContainerTO flexContainer: containerList) {
            flexQty += flexContainer.getFlexQty();
        }

        int importingInProgress = flexQty - (statisticTO.getAtBaseWarehouse() + statisticTO.getAtWrittenWarehouse() + statisticTO.getAtReserveWarehouse() + statisticTO.getExported());

        statisticTO.setImportFlexInProgress( importingInProgress );
        return statisticTO;
    }

    @Override
    public List<FlexWarehouse> getWarehouses() {
        return flexWarehouseDao.getAllWarehouses();
    }

    @Override
    public List<FlexOrderTO> getExportOrdersWithStatistic(String searchString) {
        return flexOrderDao.getExportOrdersWithStatistic(searchString, getLastUpdatedDateByConfig());
    }

    @Override
    public List<FlexOrderTO> getMountedOrdersWithStatistic(String searchString) {
        return flexOrderDao.getMountedOrdersWithStatistic(searchString, getLastUpdatedDateByConfig());
    }

    @Override
    public List<FlexOrderTO> getAllFlexOrdersWithStatistic(com.tmw.tracking.domain.flex.to.SearchFilterTO filter) {
        return flexOrderDao.getAllOrdersWithStatistic(filter);
    }


    @Override
    public void importFlex(Flex flex) {
        //validate status
        Flex existingFlex = flexDao.getBySerialNumber(flex.getSerialNumber());
        if (existingFlex != null && !existingFlex.isDeleted()) {
            throw new ValidationException("Flex already exists");
        }
        if (flex.getImportContainer() == null) {
            throw new ValidationException("Import container is not found");
        }
        if (FlexStatusEnum.COMPLETED == flex.getImportContainer().getStatus()) {
            throw new ValidationException("Trying to add flex from completed container");
        }
        //import
        flex.setImportDate(new Date());
        flex.setWarehouse(flexWarehouseDao.getBaseWarehouse());
        if (existingFlex != null && existingFlex.isDeleted()) {
            existingFlex.setImportDate(new Date());
            existingFlex.setWarehouse(flexWarehouseDao.getBaseWarehouse());
            existingFlex.setDeleted(false);
            existingFlex.setImportContainer(flex.getImportContainer());
            flexDao.update(existingFlex);
        } else {
            flexDao.add(flex);
        }
        //update container status, check for completed state
        processContainerAndOrderAfterAddingFlex(flex.getImportContainer());
        processFlexHistory(new ArrayList<Flex>(){{add(flex);}});
    }

    @Override
    public void importBatchFlex(FlexContainer flexContainer, List<String> serialNumberList) {

        if (FlexStatusEnum.COMPLETED == flexContainer.getStatus()) {
            throw new ValidationException("Trying to add flex from completed container");
        }

        List<Flex> existingFlexList = getBySerialNumbers(new ArrayList<>(serialNumberList));
        Map<String, Flex> flexMap = new HashMap<>();
        List<String> errorMessages = new ArrayList<>();
        List<String> existingFlexNumbers = new ArrayList<>();
        if (!existingFlexList.isEmpty()) {
            for (Flex flex: existingFlexList) {
                if (!flex.isDeleted()) {
                    existingFlexNumbers.add(flex.getSerialNumber());
                } else {
                    flexMap.put(flex.getSerialNumber(), flex);
                }

            }
            if (!existingFlexNumbers.isEmpty()) {
                errorMessages.add("Flex already exists " + String.join(", ", existingFlexNumbers));
            }
        }

        List<Flex> toUpdateList = new ArrayList<>();
        List<Flex> toAddList = new ArrayList<>();

        Integer existingFlexesQty = flexDao.getImportedFlexCountForContainer(flexContainer);
        Integer flexesQty = serialNumberList.size();
        if (flexesQty + existingFlexesQty > flexContainer.getImportFlexQty()) {
            throw new ValidationException("Number of flexes exceeds the container import qty.");
        }

        for (String flexSerialNum: serialNumberList) {
            if (existingFlexNumbers.contains(flexSerialNum)) {
                continue;
            }
            Flex flex = flexMap.get(flexSerialNum);
            if (flex == null) {
                flex = new Flex();
                flex.setSerialNumber(flexSerialNum);
                toAddList.add(flex);
            }
            flex.setImportContainer(flexContainer);
            flex.setImportDate(new Date());
            flex.setWarehouse(flexWarehouseDao.getBaseWarehouse());
            flex.setDeleted(false);
            toUpdateList.add(flex);
        }

        flexDao.updateBatch(toAddList, toUpdateList);
        processContainerAndOrderAfterAddingFlex(flexContainer);

        processFlexHistory(new ArrayList<Flex>(){{addAll(toAddList);addAll(toUpdateList);}});

        if (!errorMessages.isEmpty()) {
            throw new ValidationException(errorMessages);
        }
    }

    @Override
    public Map<String, List<String>> getFlexListForImportContainer(String containerNum) {
        List<FlexTO> flexList = flexDao.getImportedFlexes(getLastUpdatedDateByConfig(), containerNum);
        return prepareFlexMapGroupedByWarehouse(flexList);
    }

    @Override
    public void removeFlex(String serialNum) {
        List<String> errorMessages = new ArrayList<>();
        Flex flex = flexDao.getBySerialNumber(serialNum);
        if (flex == null) {
            throw new ValidationException("Flex is not found by serial num: " + serialNum);
        }
        if (validateOnRemove(flex, errorMessages)) {
            FlexContainer importContainer = flex.getImportContainer();
            flexDao.removeFlex(flex);
            if (importContainer != null) {
                processContainerAndOrderAfterAddingFlex(importContainer);
            }
        } else {
            throw new ValidationException(errorMessages);
        }

    }

    private Boolean validateOnRemove(Flex flex, List<String> messages) {
        if (flex.getExportOrder() != null) {
            messages.add("Flex is already in inappropriate stage (exported/mounted). Order : " + flex.getExportOrder().getOrderNumber());
            return false;
        }
        if (flex.isDeleted()) {
            messages.add("Flex has been already removed");
            return false;
        }
        return true;
    }

    @Override
    public void removeFlexBatch(List<String> serialNumList) {
        List<String> errorMessages = new ArrayList<>();
        List<Flex> flexList = getBySerialNumbers(serialNumList);
        Map<String, Flex> flexMap = new HashMap<>();
        for (Flex flex: flexList) {
            flexMap.put(flex.getSerialNumber(), flex);
        }

        Set<FlexContainer> processContainers = new HashSet<>();
        Set<Flex> flexesToRemove = new HashSet<>();

        for (String serialNum: serialNumList) {
            Flex flex = flexMap.get(serialNum);
            if (flex == null) {
                errorMessages.add("Flex is not found by serial num: " + serialNum);
            } else {
                if (validateOnRemove(flex, errorMessages)) {
                    FlexContainer importContainer = flex.getImportContainer();
                    flexesToRemove.add(flex);
                    if (importContainer != null) {
                        processContainers.add(importContainer);
                    }
                }
            }
        }
        flexDao.removeFlexBatch(flexesToRemove);
        for (FlexContainer container: processContainers) {
            processContainerAndOrderAfterAddingFlex(container);
        }
        if (!errorMessages.isEmpty()) {
            throw new ValidationException(errorMessages);
        }

    }

    @Override
    public List<String> getRemovedFlexList() {
        return flexDao.getRemovedFlexList();
    }

    private Boolean validateOnAttachToOrder(Integer flexCount, Flex flex, FlexOrder flexOrder, Set<String> messages) {
        FlexWarehouse writtenoffWarehouse = flexWarehouseDao.getWriteOffWarehouse();
        if (FlexStatusEnum.NEW == flexOrder.getStatus() && FlexOrderTypeEnum.MOUNT == flexOrder.getOrderType()) {
            messages.add("Trying to attach flex to order in mounted state");
            return false;
        }
        if (flexCount >= flexOrder.getExportFlexQty()) {
            messages.add("The number of attached flex is more than set for the order (" + flexOrder.getExportContainerQty() + ")");
        }
        if (FlexOrderTypeEnum.EXPORT != flexOrder.getOrderType() && FlexOrderTypeEnum.MOUNT != flexOrder.getOrderType()) {
            messages.add("Order " + flexOrder.getOrderNumber() + " is not of Export/Mount type");
            return false;
        }
        if (flex.isDeleted()) {
            messages.add("Flex " + flex.getSerialNumber() + " has been removed");
            return false;
        }
        if (flex.getWarehouse().equals(writtenoffWarehouse) || flex.getExportOrder() != null || flex.getMountContainer() != null) {
            String warehouse = (flex.getWarehouse() != null? " Located at " + flex.getWarehouse().getName() : "");
            String mountContainer = flex.getMountContainer() != null? " ; Mount Container: " + flex.getMountContainer().getContainerNumber(): "";
            messages.add("Flex " + flex.getSerialNumber() + " is not in proper state. " + warehouse + mountContainer);
            return false;
        }
        return true;
    }

    @Override
    public void attachFlexToOrder(String serialNum, String orderNum) {
        //validation
        FlexOrder flexOrder = getOrderByNumber(orderNum);
        if (flexOrder == null) {
            throw new ValidationException("Order is not found by order num: " + orderNum);
        }
        Flex flex = flexDao.getBySerialNumber(serialNum);
        if (flex == null) {
            throw new ValidationException("Flex is not found by serial num: " + serialNum);
        }

        Set<String> errorMessages = new HashSet<>();
        Integer flexCount = flexDao.getExportedFlexCountForOrder(flexOrder);
        if (validateOnAttachToOrder(flexCount, flex, flexOrder, errorMessages)) {
            // attach
            flex.setExportOrder(flexOrder);
            flex.setExportDate(new Date());
            updateFlex(flex);
            flexCount++;
            //update container status, check for completed state

            if (flexCount.equals(flexOrder.getExportContainerQty())) {
                flexOrder.setStatus(FlexStatusEnum.NEW); //coming into mount stage
                flexOrder.setOrderType(FlexOrderTypeEnum.MOUNT);
                flexOrderDao.update(flexOrder);
            } else if (flexOrder.getStatus() != FlexStatusEnum.IN_PROGRESS) {
                flexOrder.setStatus(FlexStatusEnum.IN_PROGRESS);
                flexOrderDao.update(flexOrder);
            }
        } else {
            throw new ValidationException(errorMessages);
        }

    }

    @Override
    public void attachFlexToOrderBatch(FlexController.OrderFlexBatchParams orderFlexBatchParams) {
        FlexOrder flexOrder = getOrderByNumber(orderFlexBatchParams.getOrderNum());
        if (flexOrder == null) {
            throw new ValidationException("Order is not found by order num: " + orderFlexBatchParams.getOrderNum());
        }

        List<Flex> flexList = flexDao.getBySerialNumbers(orderFlexBatchParams.getSerialNumbers());
        Map<String, Flex> flexMap = new HashMap<>();
        for (Flex flex: flexList) {
            flexMap.put(flex.getSerialNumber(), flex);
        }
        Integer flexCount = flexDao.getExportedFlexCountForOrder(flexOrder);

        Set<String> errorMessages = new HashSet<>();
        List<Flex> flexesToUpdate = new ArrayList<>();

        for (String serialNum: orderFlexBatchParams.getSerialNumbers()) {
            Flex flex = flexMap.get(serialNum);
            if (flex == null) {
                errorMessages.add("Flex is not found by serial num: " + serialNum);
                continue;
            }

            if (validateOnAttachToOrder(flexCount, flex, flexOrder, errorMessages)) {
                // attach
                flex.setExportOrder(flexOrder);
                flex.setExportDate(new Date());
                flexesToUpdate.add(flex);
                //update container status, check for completed state
                flexCount ++;
            }
        }
        flexDao.updateBatch(new ArrayList<>(), flexesToUpdate);
        //update order
        if (flexCount.equals(flexOrder.getExportFlexQty())) {
            flexOrder.setStatus(FlexStatusEnum.NEW); //coming into mount stage
            flexOrder.setOrderType(FlexOrderTypeEnum.MOUNT);
            flexOrderDao.update(flexOrder);
        } else if (flexOrder.getStatus() != FlexStatusEnum.IN_PROGRESS) {
            flexOrder.setStatus(FlexStatusEnum.IN_PROGRESS);
            flexOrderDao.update(flexOrder);
        }
        if (!errorMessages.isEmpty()) {
            throw new ValidationException(errorMessages);
        }
    }

    @Override
    public Map<String, List<String>> getFlexListForExportOrder(String orderNum) {
        List<FlexTO> flexList = flexDao.getExportFlexes(getLastUpdatedDateByConfig(), orderNum);
        return prepareFlexMapGroupedByWarehouse(flexList);
    }

    @Override
    public List<String> getFlexListForReservedWarehouse() {
        List<String> result = new ArrayList<>();
        for (FlexTO flex: flexDao.getReservedFlexes()) {
            result.add(flex.getSerialNumber());
        }
        return result;
    }

    @Override
    public List<String> getFlexListForBaseWarehouse() {
        List<String> result = new ArrayList<>();
        for (FlexTO flex: flexDao.getBaseFlexes()) {
            if (flex.getMountContainer() == null) {
                result.add(flex.getSerialNumber());
            }
        }
        return result;
    }

    @Override
    public List<FlexTO> getFlexEntitiesForExportOrder(String orderNum) {
        return flexDao.getExportFlexes(getLastUpdatedDateByConfig(), orderNum);
    }

    @Override
    public List<FlexOrder> getAvailableOrdersForMount(String searchString) {
        return flexOrderDao.getOrdersForMount(searchString);
    }

    private Boolean validateOnAttachToContainer(Integer flexCount, FlexOrder flexOrder, Flex flex, Set<String> messages) {
        if (flex.isDeleted()) {
            messages.add("Flex has been removed");
            return false;
        }
        //FlexWarehouse baseWarehouse = flexWarehouseDao.getBaseWarehouse();
        //FlexWarehouse reserveWarehouse = flexWarehouseDao.getReserveWarehouse();
        FlexWarehouse writtenOffWarehouse = flexWarehouseDao.getWriteOffWarehouse();
        if (flex.getWarehouse().equals(writtenOffWarehouse)) {
            String warehouse = (flex.getWarehouse() != null? " Located at " + flex.getWarehouse().getName() : "");
            messages.add("Flex " + flex.getSerialNumber() + " is not in proper warehouse." + warehouse);
            return false;
        }
        if (flexCount >= flexOrder.getExportContainerQty()) {
            messages.add("The number of attached flex is more than set for the order " + flexOrder.getOrderNumber() + " (" + flexOrder.getExportContainerQty() + ")");
            return false;
        }
        if (flex.getExportOrder() == null) {
            messages.add("Flex " + flex.getSerialNumber() + " does not have an export order assigned");
            return false;
        }
        if (flex.getMountContainer() != null) {
            messages.add("Flex " + flex.getSerialNumber() + " is already attached to container " + flex.getMountContainer().getContainerNumber());
            return false;
        }
        if (FlexStatusEnum.COMPLETED == flex.getExportOrder().getStatus()) {
            messages.add("Flex order " + flex.getExportOrder().getOrderNumber() + " is not in proper state (" + flex.getExportOrder().getStatus() + ")");
            return false;
        }
        if (flex.isDeleted()) {
            messages.add("Flex " + flex.getSerialNumber() + " has been removed");
            return false;
        }
        return true;
    }

    private Boolean validateOnAttachToContainerBatch(Integer flexCount, FlexOrder flexOrder, Flex flex, Set<String> messages) {
        if (flex.isDeleted()) {
            messages.add("Flex has been removed");
            return false;
        }
        FlexWarehouse writtenOffWarehouse = flexWarehouseDao.getWriteOffWarehouse();
        if (flex.getWarehouse().equals(writtenOffWarehouse)) {
            String warehouse = (flex.getWarehouse() != null? " Located at " + flex.getWarehouse().getName() : "");
            messages.add("Flex " + flex.getSerialNumber() + " is not in proper warehouse." + warehouse);
            return false;
        }
        if (flex.getExportOrder() == null) {
            messages.add("Flex " + flex.getSerialNumber() + " does not have an export order assigned");
            return false;
        }
        if (flex.isDeleted()) {
            messages.add("Flex " + flex.getSerialNumber() + " has been removed");
            return false;
        }
        return true;
    }

    @Override
    public void attachFlexToContainer(String serialNum, String containerNum) {

        Flex flex = flexDao.getBySerialNumber(serialNum);
        if (flex == null) {
            throw new ValidationException("Flex is not found by serial num: " + serialNum);
        }
        FlexOrder exportOrder = flex.getExportOrder();
        Integer flexCount = flexDao.getMountedFlexCountForOrder(exportOrder);

        Set<String> errorMessages = new HashSet<>();
        if (validateOnAttachToContainer(flexCount, exportOrder, flex, errorMessages)) {
            FlexContainer flexContainer = flexContainerDao.getContainerByNumber(containerNum);
            if (flexContainer == null) {
                //create container
                flexContainer = new FlexContainer();
                flexContainer.setStatus(FlexStatusEnum.NEW);
                flexContainer.setContainerNumber(containerNum);
                flexContainer = flexContainerDao.add(flexContainer);
            }
            //TODO check if container already used
            if (flexContainer.getImportOrder() != null) {
                throw new ValidationException("Illegal container chosen, it was used as Import Container");
            }

            // attach
            flex.setMountContainer(flexContainer);
            flex.setMountDate(new Date());

            updateFlex(flex);
            flexCount++;
            //update container status, check for completed state

            if (flexCount.equals(exportOrder.getExportContainerQty())) {
                exportOrder.setStatus(FlexStatusEnum.COMPLETED);
                exportOrder.setOrderType(FlexOrderTypeEnum.MOUNT);
                flexOrderDao.update(exportOrder);
            } else if (exportOrder.getStatus() != FlexStatusEnum.IN_PROGRESS) {
                exportOrder.setStatus(FlexStatusEnum.IN_PROGRESS);
                exportOrder.setOrderType(FlexOrderTypeEnum.MOUNT);
                flexOrderDao.update(exportOrder);
            }
        } else {
            throw new ValidationException(errorMessages);
        }
    }

    @Override
    public void attachFlexToContainerBatch(FlexController.FlexInContainerBatchParams params) {

        //preparation for all the staff
        Map<String, String> serialNumberToContainerMap = new HashMap<>();
        // Цей Set тепер просто збирає унікальні номери контейнерів для запиту в БД
        Set<String> containerNumberSet = new HashSet<>();
        Set<String> errorMessages = new HashSet<>();
        Set<String> orderNumberSet = new HashSet<>();

        for (FlexController.ContainerFlexParams param: params.getFlexInContainerList()) {
            // Збираємо унікальні контейнери без генерації помилок
            containerNumberSet.add(param.getСontainerNum());

            // Перевіряємо чи не дублюється сам флекс у вхідному запиті
            String previousContainer = serialNumberToContainerMap.put(param.getSerialNumber(), param.getСontainerNum());
            if (previousContainer != null) {
                errorMessages.add("Flex " + param.getSerialNumber() + " has been already mentioned, for " + previousContainer + " container number");
            }
        }

        if (serialNumberToContainerMap.isEmpty()) {
            throw new ValidationException("No data to process");
        }

        List<Flex> flexList = flexDao.getBySerialNumbers(new ArrayList<>(serialNumberToContainerMap.keySet()));
        // Передаємо лише унікальні номери контейнерів, використовуючи containerNumberSet замість values() мапи
        List<FlexContainer> flexContainers = flexContainerDao.getContainersByNumbers(new ArrayList<>(containerNumberSet));

        Map<String, Flex> flexMap = new HashMap<>();
        if (flexList.isEmpty()) {
            errorMessages.add("No flexes was found to process");
            throw new ValidationException(errorMessages);
        }
        for (Flex flex: flexList) {

            if (flex.getExportOrder() == null) {
                errorMessages.add("Flex " + flex.getSerialNumber() + " does not have an export order assigned");
            } else {
                flexMap.put(flex.getSerialNumber(), flex);
                orderNumberSet.add(flex.getExportOrder().getOrderNumber());
                if (orderNumberSet.size() > 1) {
                    throw new ValidationException("The batch contains flexes for different order set!");
                }
            }
        }
        Map<String, FlexContainer> flexContainerMap = new HashMap<>();
        for (FlexContainer flexContainer: flexContainers) {
            flexContainerMap.put(flexContainer.getContainerNumber(), flexContainer);
        }
        FlexOrder exportOrder = flexList.get(0).getExportOrder();
        if (exportOrder == null) {
            throw new ValidationException("The export order for the flex is missing! Flex is in the incorrect state");
        }
        Integer flexCount = flexDao.getMountedFlexCountForOrder(exportOrder);

        //attaching
        List<FlexContainer> containersToAdd = new ArrayList<>();
        List<Flex> flexToUpdate = new ArrayList<>();
        for (FlexController.ContainerFlexParams param: params.getFlexInContainerList()) {
            Flex flex = flexMap.get(param.getSerialNumber());
            if (flex == null) {
                errorMessages.add("Flex is not found by serial num: " + param.getSerialNumber());
                continue;
            }

            if (validateOnAttachToContainerBatch(flexCount, exportOrder, flex, errorMessages)) {
                FlexContainer flexContainer = flexContainerMap.get(param.getСontainerNum());
                if (flexContainer == null) {
                    //create container
                    flexContainer = new FlexContainer();
                    flexContainer.setStatus(FlexStatusEnum.NEW);
                    flexContainer.setContainerNumber(param.getСontainerNum());
                    containersToAdd.add(flexContainer);
                    flexContainerMap.put(param.getСontainerNum(), flexContainer);
                }
                //TODO check if container already used
                if (flexContainer.getImportOrder() != null) {
                    errorMessages.add("Illegal container chosen = " + flexContainer.getContainerNumber() + " , it was used as Import Container");
                    continue;
                }

                // attach
                if (flex.getMountContainer() != null && flex.getMountContainer().getId().equals(flexContainer.getId())) {
                    // already mounted to a container, no need to attach again
                } else {
                    flex.setMountContainer(flexContainer);
                    flex.setMountDate(new Date());

                    flexToUpdate.add(flex);
                    flexCount++;
                }
            }
        }

        flexContainerDao.upsertAll(containersToAdd);
        flexDao.updateBatch(new ArrayList<>(), flexToUpdate);
        //update container status, check for completed state
        if (flexCount.equals(exportOrder.getExportFlexQty())) {
            exportOrder.setStatus(FlexStatusEnum.COMPLETED);
            flexOrderDao.update(exportOrder);
        } else if (exportOrder.getStatus() != FlexStatusEnum.IN_PROGRESS) {
            exportOrder.setStatus(FlexStatusEnum.IN_PROGRESS);
            flexOrderDao.update(exportOrder);
        }

        if (!errorMessages.isEmpty()) {
            throw new ValidationException(errorMessages);
        }

    }

    private Boolean validateIsFlexDetachable(Flex flex, List<String> errorMessages) {

        FlexWarehouse writtenOffWarehouse = flexWarehouseDao.getWriteOffWarehouse();
        if (flex.isDeleted()) {
            errorMessages.add("Flex has been removed");
            return false;
        }
        if (flex.getWarehouse().equals(writtenOffWarehouse)) {
            String warehouse = (flex.getWarehouse() != null? " Located at " + flex.getWarehouse().getName() : "");
            errorMessages.add("Flex " + flex.getSerialNumber() + " is not in proper warehouse. " + warehouse);
            return false;
        }
        if (flex.getExportOrder() == null) {
            errorMessages.add("Flex " + flex.getSerialNumber() + " does not have an export order assigned");
            return false;
        }
        return true;

    }
    @Override
    public void detachFlexFromContainer(String serialNumber) {

        Flex flex = flexDao.getBySerialNumber(serialNumber);
        if (flex == null) {
            throw new ValidationException("Flex is not found by serial num: " + serialNumber);
        }
        List<String> errorMessages = new ArrayList<>();
        if (!validateIsFlexDetachable(flex, errorMessages)) {
            throw new ValidationException(errorMessages);
        }

        if (flex.getMountContainer() == null) {
            throw new ValidationException("Flex " + serialNumber + " is not attached to container");
        }

        flex.setMountContainer(null);
        flex.setMountDate(null);

        updateFlex(flex);
        //update container status, check for completed state
        FlexOrder exportOrder = flex.getExportOrder();
        exportOrder.setStatus(FlexStatusEnum.IN_PROGRESS);
        flexOrderDao.update(exportOrder);
    }

    @Override
    public void detachFlexFromContainerBatch(List<String> validatedSerialNumbers) {
        List<Flex> flexToUpdate = new ArrayList<>();
        List<String> errorMessages = new ArrayList<>();
        List<Flex> flexes = flexDao.getBySerialNumbers(validatedSerialNumbers);
        Map<String, Flex> flexMap = new HashMap<>();
        Set<FlexOrder> flexOrderSet = new HashSet<>();
        for (Flex flex: flexes) {
            flexMap.put(flex.getSerialNumber(), flex);
        }
        for (String serialNum: validatedSerialNumbers) {
            Flex flex = flexMap.get(serialNum);
            if (flex == null) {
                errorMessages.add("Flex " + serialNum + " is not found");
                continue;
            }
            if (!validateIsFlexDetachable(flex, errorMessages)) {
                continue;
            }
            if (flex.getMountContainer() == null) {
                errorMessages.add("Flex " + serialNum + " is not attached to container");
                continue;
            }
            flexOrderSet.add(flex.getExportOrder());
            if (flexOrderSet.size() > 1) {
                throw new ValidationException("The flexes are from different set of orders!");
            }
            if (FlexOrderTypeEnum.MOUNT != flex.getExportOrder().getOrderType()) {
                throw new ValidationException("The order " + flex.getExportOrder().getOrderNumber() + " is not of Mount type!");
            }
            flex.setMountContainer(null);
            flex.setMountDate(null);
            flexToUpdate.add(flex);
        }

        if (!flexToUpdate.isEmpty()) {
            flexDao.updateBatch(new ArrayList<>(), flexToUpdate);
            FlexOrder exportOrder = flexToUpdate.get(0).getExportOrder();
            exportOrder.setStatus(FlexStatusEnum.IN_PROGRESS);
            flexOrderDao.update(exportOrder);
        }
        if (!errorMessages.isEmpty()) {
            throw new ValidationException(errorMessages);
        }
    }

    @Override
    public void detachFlexFromOrder(String serialNumber) {
        Flex flex = flexDao.getBySerialNumber(serialNumber);
        if (flex == null) {
            throw new ValidationException("Flex is not found by serial num: " + serialNumber);
        }
        List<String> errorMessages = new ArrayList<>();
        if (!validateIsFlexDetachable(flex, errorMessages)) {
            throw new ValidationException(errorMessages);
        }


        //update container status, check for completed state
        FlexOrder exportOrder = flex.getExportOrder();
        exportOrder.setStatus(FlexStatusEnum.IN_PROGRESS);
        exportOrder.setOrderType(FlexOrderTypeEnum.EXPORT);

        flexOrderDao.update(exportOrder);

        flex.setExportOrder(null);
        flex.setExportDate(null);
        flex.setMountContainer(null);
        flex.setMountDate(null);
        updateFlex(flex);

    }

    @Override
    public void detachFlexFromOrderBatch(List<String> validatedSerialNumbers) {
        List<Flex> flexToUpdate = new ArrayList<>();
        List<String> errorMessages = new ArrayList<>();
        List<Flex> flexes = flexDao.getBySerialNumbers(validatedSerialNumbers);
        FlexOrder exportOrder = null;
        Map<String, Flex> flexMap = new HashMap<>();
        Set<FlexOrder> flexOrderSet = new HashSet<>();
        for (Flex flex: flexes) {
            flexMap.put(flex.getSerialNumber(), flex);
        }
        for (String serialNum: validatedSerialNumbers) {
            Flex flex = flexMap.get(serialNum);
            if (flex == null) {
                errorMessages.add("Flex " + serialNum + " is not found");
                continue;
            }
            if (!validateIsFlexDetachable(flex, errorMessages)) {
                continue;
            }
            flexOrderSet.add(flex.getExportOrder());
            if (flexOrderSet.size() > 1) {
                throw new ValidationException("The flexes are from different set of orders!");
            }
            /*if (FlexOrderTypeEnum.MOUNT != flex.getExportOrder().getOrderType()) {
                throw new ValidationException("The order " + flex.getExportOrder().getOrderNumber() + " is not of Mount type!");
            }*/
            exportOrder = flex.getExportOrder();
            flex.setExportOrder(null);
            flex.setExportDate(null);
            flex.setMountContainer(null);
            flex.setMountDate(null);
            flexToUpdate.add(flex);
        }

        if (!flexToUpdate.isEmpty()) {
            flexDao.updateBatch(new ArrayList<>(), flexToUpdate);
            exportOrder.setStatus(FlexStatusEnum.IN_PROGRESS);
            exportOrder.setOrderType(FlexOrderTypeEnum.EXPORT);
            flexOrderDao.update(exportOrder);
        }
        if (!errorMessages.isEmpty()) {
            throw new ValidationException(errorMessages);
        }

    }

    @Override
    public void writeOffFlex(String serialNum) {
        Flex flexToWriteOff = flexDao.getBySerialNumber(serialNum);
        if (flexToWriteOff == null) {
            throw new ValidationException("Flex " + serialNum + " is not found");
        }
        if (flexToWriteOff.isDeleted()) {
            throw new ValidationException("Flex has been removed");
        }
        if (flexToWriteOff.getExportOrder() != null) {
            throw new ValidationException("Flex is attached to order and not located on a warehouse");
        }
        flexToWriteOff.setWriteOffDate(new Date());
        moveFlexToWarehouse(flexToWriteOff, flexWarehouseDao.getWriteOffWarehouse());
    }

    @Override
    public void cancelWriteOffFlex(String serialNum) {
        Flex flexToWriteOff = flexDao.getBySerialNumber(serialNum);
        if (flexToWriteOff == null) {
            throw new ValidationException("Flex " + serialNum + " is not found");
        }
        if (flexToWriteOff.isDeleted()) {
            throw new ValidationException("Flex has been removed");
        }
        flexToWriteOff.setWriteOffDate(null);
        moveFlexToWarehouse(flexToWriteOff, flexWarehouseDao.getBaseWarehouse());
    }

    @Override
    public void reserveFlex(String serialNum) {
        Flex flexToMove = flexDao.getBySerialNumber(serialNum);
        if (flexToMove == null) {
            throw new ValidationException("Flex " + serialNum + " is not found");
        }
        if (flexToMove.isDeleted()) {
            throw new ValidationException("Flex has been removed");
        }
        moveFlexToWarehouse(flexToMove, flexWarehouseDao.getReserveWarehouse());

        processFlexHistory(new ArrayList<Flex>(){{add(flexToMove);}});
    }

    @Override
    public void unReserveFlex(String serialNum) {
        Flex flexToMove = flexDao.getBySerialNumber(serialNum);
        if (flexToMove == null) {
            throw new ValidationException("Flex " + serialNum + " is not found");
        }
        if (flexToMove.isDeleted()) {
            throw new ValidationException("Flex has been removed");
        }
        moveFlexToWarehouse(flexToMove, flexWarehouseDao.getBaseWarehouse());
        processFlexHistory(new ArrayList<Flex>(){{add(flexToMove);}});
    }

    @Override
    public void reserveFlexBatch(ArrayList<String> serialNums) {
        moveFlexBatch(serialNums, flexWarehouseDao.getReserveWarehouse());
    }

    @Override
    public void unReserveFlexBatch(ArrayList<String> serialNums) {
        moveFlexBatch(serialNums, flexWarehouseDao.getBaseWarehouse());
    }

    private void moveFlexBatch(ArrayList<String> serialNums, FlexWarehouse moveToWarehouse) {
        List<Flex> toUpdate = new ArrayList<>();
        List<Flex> existingFlexList = getBySerialNumbers(new ArrayList<>(serialNums));
        Map<String, Flex> flexMap = new HashMap<>();
        List<String> errorMessages = new ArrayList<>();
        Set<String> incorrectSerials = new HashSet<>();
        for (Flex flex: existingFlexList) {
            if (flex.getWarehouse().getId().equals(moveToWarehouse.getId())) {
                errorMessages.add("Flex " + flex.getSerialNumber() + " already in the " + moveToWarehouse.getName());
                incorrectSerials.add(flex.getSerialNumber());
            } else if (flex.isDeleted()) {
                errorMessages.add("Flex " + flex.getSerialNumber() + " is deleted ");
                incorrectSerials.add(flex.getSerialNumber());
            } else {
                flexMap.put(flex.getSerialNumber(), flex);
            }
        }
        for (String serialNum: serialNums) {
            Flex flex = flexMap.get(serialNum);
            if (flex == null && !incorrectSerials.contains(serialNum)) {
                errorMessages.add("Flex " + serialNum + " is not found");
            } else if (flex != null) {
                flex.setWarehouse(moveToWarehouse);
                toUpdate.add(flex);
            }
        }
        flexDao.updateBatch(new ArrayList<>(), toUpdate);
        processFlexHistory(new ArrayList<Flex>(){{addAll(toUpdate);}});

        if (!errorMessages.isEmpty()) {
            throw new ValidationException(errorMessages);
        }
    }


    @Override
    public void setReservedFlexToOrder(String serialNum, String orderNum) {
        Flex flexToMove = flexDao.getBySerialNumber(serialNum);
        if (flexToMove == null) {
            throw new ValidationException("Flex " + serialNum + " is not found");
        }
        FlexOrder exportOrder = flexOrderDao.getOrderByNumber(orderNum);
        if (exportOrder == null) {
            throw new ValidationException("Order " + orderNum + " is not found");
        }
        if (exportOrder.getOrderType() != FlexOrderTypeEnum.EXPORT) {
            throw new ValidationException("Order " + orderNum + " is not of Export type");
        }
        if (exportOrder.getStatus() == FlexStatusEnum.COMPLETED) {
            throw new ValidationException("Order " + orderNum + " is already completed");
        }
        if (!flexToMove.getWarehouse().getId().equals(flexWarehouseDao.getReserveWarehouse().getId())) {
            throw new ValidationException("Flex " + serialNum + " is not reserved");
        }
        if (flexToMove.isDeleted()) {
            throw new ValidationException("Flex has been removed");
        }
        flexToMove.setWarehouse(flexWarehouseDao.getBaseWarehouse());
        flexToMove.setExportOrder(exportOrder);
        flexDao.update(flexToMove);
    }

    @Override
    public void renameFlex(String oldSerialNum, String newSerialNum) {
        Flex flexToRename = flexDao.getBySerialNumber(oldSerialNum);
        if (flexToRename == null) {
            throw new ValidationException("Flex " + oldSerialNum + " is not found");
        }
        if (flexToRename.getExportOrder() != null && flexToRename.getExportOrder().getStatus() == FlexStatusEnum.COMPLETED) {
            throw new ValidationException("Flex " + oldSerialNum + " can not be renamed, it is attached already to completed order");
        }
        if (flexToRename.isDeleted()) {
            throw new ValidationException("Flex has been removed");
        }
        FlexRename flexRename = new FlexRename();
        flexRename.setRelatedFlex(flexToRename);
        flexRename.setOldSerialNumber(oldSerialNum);
        flexRename.setNewSerialNumber(newSerialNum);
        flexDao.updateFlexRename(flexRename);

        flexToRename.setSerialNumber(newSerialNum);
        flexDao.update(flexToRename);
        //add history

    }

    @Override
    public Map<String, List<FlexTO>> flexListMountedToContainers(String orderNum) {
        List<FlexTO> flexList = flexDao.getMountedFlexes(getLastUpdatedDateByConfig(), orderNum);
        return prepareFlexMapGroupedByOrder(flexList);
    }

    @Override
    public List<FlexHistoryTO> getFlexWarehouseMovement(String flexSerialNum) {
        return flexHistoryDao.getHistory(flexSerialNum, getLastUpdatedDateByConfig());
    }

    @Override
    public void updateFlex(Flex flex) {
        flexDao.update(flex);
    }

    @Override
    public FlexOrder getOrderByNumber(String orderNumber) {
        return flexOrderDao.getOrderByNumber(orderNumber);
    }

    @Override
    public List<FlexOrder> getOrdersByNumber(List<String> orderNumbers) {
        return flexOrderDao.getOrdersByNumbers(orderNumbers);
    }

    @Override
    public FlexOrder addOrder(FlexOrder order) {
        return flexOrderDao.add(order);
    }

    private List<FlexOrderTO> mapImportedOrders(List<Flex> flexes) {
        Set<FlexOrderTO> orderSet = new HashSet<FlexOrderTO>();

        for (Flex flex: flexes) {
            FlexWarehouseTO warehouseTO = new FlexWarehouseTO();
            warehouseTO.setName(flex.getWarehouse().getName());

            FlexOrderTO orderTO = getOrderByNum(orderSet, flex.getImportContainer().getImportOrder().getOrderNumber());
            if (orderTO == null) {
                orderTO = new FlexOrderTO();
                orderTO.setOrderNumber(flex.getImportContainer().getImportOrder().getOrderNumber());
                orderTO.setOrderType(flex.getImportContainer().getImportOrder().getOrderType());
                orderTO.setStatus(flex.getImportContainer().getImportOrder().getStatus());
                orderTO.setUpdatedDate(flex.getImportContainer().getImportOrder().getLastUpdated());
            }
            FlexContainerTO container = orderTO.getContainerByNum(flex.getImportContainer().getContainerNumber());
            if (container == null) {
                container = new FlexContainerTO();
                container.setContainerNumber(flex.getImportContainer().getContainerNumber());
                container.setFlexQty(flex.getImportContainer().getImportFlexQty());
                container.setOrder(orderTO);
                container.setStatus(flex.getImportContainer().getStatus());
                container.setUpdatedDate(flex.getImportContainer().getLastUpdated());
            }
            FlexTO flexTO = new FlexTO();
            flexTO.setSerialNumber(flex.getSerialNumber());
            flexTO.setImportDate(flex.getImportDate());
            flexTO.setImportContainer(container);
            flexTO.setWarehouse(warehouseTO);
            flexTO.setUpdatedDate(flex.getLastUpdated());
            container.getFlexes().add(flexTO);
            orderTO.getContainers().add(container);

            orderSet.add(orderTO);
        }

        return new ArrayList<>(orderSet);
    }

    private List<FlexOrderTO> mapExportedOrders(List<Flex> flexes) {
        Set<FlexOrderTO> orderSet = new HashSet<FlexOrderTO>();

        for (Flex flex: flexes) {
            FlexWarehouseTO warehouseTO = new FlexWarehouseTO();
            warehouseTO.setName(flex.getWarehouse().getName());
            FlexOrderTO orderTO = getOrderByNum(orderSet, flex.getExportOrder().getOrderNumber());
            if (orderTO == null) {
                orderTO = new FlexOrderTO();
                orderTO.setOrderNumber(flex.getExportOrder().getOrderNumber());
                orderTO.setOrderType(flex.getExportOrder().getOrderType());
                orderTO.setStatus(flex.getExportOrder().getStatus());
                orderTO.setUpdatedDate(flex.getExportOrder().getLastUpdated());
            }
            FlexContainerTO container = orderTO.getContainerByNum(flex.getMountContainer().getContainerNumber());
            if (container == null) {
                container = new FlexContainerTO();
                container.setContainerNumber(flex.getMountContainer().getContainerNumber());
                container.setFlexQty(flex.getMountContainer().getImportFlexQty());
                container.setOrder(orderTO);
                container.setStatus(flex.getMountContainer().getStatus());
                container.setUpdatedDate(flex.getMountContainer().getLastUpdated());
            }
            FlexTO flexTO = new FlexTO();
            flexTO.setSerialNumber(flex.getSerialNumber());
            flexTO.setImportDate(flex.getImportDate());
            flexTO.setMountDate(flex.getMountDate());
            flexTO.setMountContainer(container);
            flexTO.setWarehouse(warehouseTO);
            flexTO.setUpdatedDate(flex.getLastUpdated());
            FlexWarehouse writtenOffWarehouse = flexWarehouseDao.getWriteOffWarehouse();
            if (flex.getWarehouse().equals(writtenOffWarehouse)) {
                orderTO.getWrittenOffFlexes().add(flexTO);
            } else {
                container.getFlexes().add(flexTO);
            }
            orderTO.getContainers().add(container);
            orderSet.add(orderTO);
        }

        return new ArrayList<>(orderSet);
    }

    private Map<String, List<String>> prepareFlexMapGroupedByWarehouse(List<FlexTO> flexList) {
        Map<String, List<String>> map = new HashMap<>();

        for (FlexTO flex: flexList) {
            String warehouseName = flex.getWarehouse().getName();
            List<String> warehouseFlexes = map.get(warehouseName);
            if (warehouseFlexes == null) {
                warehouseFlexes = new ArrayList<>();
            }
            warehouseFlexes.add(flex.getSerialNumber());

            map.put(warehouseName, warehouseFlexes);
        }

        return map;
    }

    private Map<String, List<FlexTO>> prepareFlexMapGroupedByOrder(List<FlexTO> flexList) {
        Map<String, List<FlexTO>> map = new HashMap<>();

        for (FlexTO flex: flexList) {
            String orderNum = flex.getExportOrder().getOrderNumber();

            List<FlexTO> orderFlexes = map.get(orderNum);
            if (orderFlexes == null) {
                orderFlexes = new ArrayList<>();
            }
            orderFlexes.add(flex);

            map.put(orderNum, orderFlexes);
        }
        return map;
    }

    private FlexOrderTO getOrderByNum(Set<FlexOrderTO> flexOrderTOList, String orderNum) {
        for (FlexOrderTO flexOrderTO: flexOrderTOList) {
            if (flexOrderTO.getOrderNumber().equals(orderNum)) {
                return flexOrderTO;
            }
        }
        return null;
    }


    private void validateFlexContainers(List<FlexContainer> containers, Boolean skipInvalid) {
        List<FlexContainer> invalidContainers = new ArrayList<>();
        for (FlexContainer flexContainer: containers) {
            try {
                validateFlexContainer(flexContainer);
            }
            catch (ValidationException e) {
                if (skipInvalid) {
                    invalidContainers.add(flexContainer);
                } else {
                    throw new ValidationException(e.getMessage());
                }
            }
        }
        containers.removeAll(invalidContainers);
    }

    private void validateFlexContainer(FlexContainer flexContainer) {
        if (flexContainer.getImportFlexQty() == null || flexContainer.getImportFlexQty() < 1) {
            throw new ValidationException("Container should have at least one flex");
        }
        if (flexContainer.getContainerNumber() == null || flexContainer.getContainerNumber().isEmpty() || !flexContainer.getContainerNumber().matches("^[a-zA-Z]{4}[0-9]{7}$")) {
            throw new ValidationException("Incorrect Container Number");
        }
    }

    private void moveFlexToWarehouse(Flex flex, FlexWarehouse toWarehouse) {
        if (flex.getWarehouse().getId().equals(toWarehouse.getId())) {
            throw new ValidationException("Flex " + flex.getSerialNumber() + " is already in the warehouse");
        }
        flex.setWarehouse(toWarehouse);
        flexDao.update(flex);
    }



    public static class FlexUtil {
        public static List<String> parseOrderNumbers(List<FlexOrder> flexOrders) {
            List<String> ids = new ArrayList<>();
            for (FlexOrder flexOrder: flexOrders) {
                ids.add(flexOrder.getOrderNumber());
            }
            return ids;
        }

        public static FlexOrder findOrderInCollection(List<FlexOrder> flexOrders, String orderNum) {
            for (FlexOrder fl: flexOrders) {
                if (fl.getOrderNumber().equals(orderNum)) {
                    return fl;
                }
            }
            return null;
        }


    }


    private void processFlexHistory(List<Flex> flexList) {
        List<FlexHistory> flexHistoryList = new ArrayList<>();
        for (Flex flex: flexList) {
            FlexHistory history = new FlexHistory();
            history.setActionDate(new Date());
            history.setFlex(flex);
            history.setWarehouse(flex.getWarehouse());

            flexHistoryList.add(history);
        }
        flexHistoryDao.batchAdd(flexHistoryList);
    }

    private void processContainerAndOrderAfterAddingFlex(FlexContainer container) {
        Integer flexCount = flexDao.getImportedFlexCountForContainer(container);
        if (flexCount.equals(container.getImportFlexQty())) {
            container.setStatus(FlexStatusEnum.COMPLETED);
            container.getImportOrder().setStatus(FlexStatusEnum.COMPLETED);
        } else {
            container.setStatus(FlexStatusEnum.IN_PROGRESS);
            container.getImportOrder().setStatus(FlexStatusEnum.IN_PROGRESS);
        }
        flexContainerDao.update(container);
        flexOrderDao.update(container.getImportOrder());

    }



}
