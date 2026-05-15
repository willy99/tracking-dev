package com.tmw.tracking.service;

import com.tmw.tracking.domain.flex.entities.*;
import com.tmw.tracking.domain.flex.to.*;
import com.tmw.tracking.web.controller.FlexController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface FlexService {

    /********************* API *********************/

    //Import stage

    List<FlexOrder> getAvailableOrdersForImport(String searchString);

    List<FlexContainerTO> getAvailableContainersForImport(String searchString);


    void importStageFrom1c(FlexImportPackage flexImportPackage); //1c->back

    List<FlexOrderTO> getImportedOrders(); //back->1c

    void importFlex(Flex flex);

    void importBatchFlex(FlexContainer flexContainer, List<String> serialNumberList);

    Map<String, List<String>> getFlexListForImportContainer(String containerNum);

    void removeFlex(String serialNum);
    void removeFlexBatch(List<String> serialNumList);

    List<String> getRemovedFlexList();


    //Export stage
    void exportStageFrom1c(FlexExportPackage flexExportPackage); //1c->back

    List<FlexOrder> getAvailableOrdersForExport(String searchString);

    void attachFlexToOrder(String serialNum, String orderNum);
    void attachFlexToOrderBatch(FlexController.OrderFlexBatchParams orderFlexBatchParams);

    Map<String, List<String>> getFlexListForExportOrder(String orderNum);

    List<String> getFlexListForReservedWarehouse();

    List<String> getFlexListForBaseWarehouse();

    List<FlexTO> getFlexEntitiesForExportOrder(String orderNum);


    //Mount stage
    List<FlexOrder> getAvailableOrdersForMount(String searchString);

    List<FlexOrderTO> getMountedOrders(); //back->1c

    void attachFlexToContainer(String serialNum, String containerNum);
    void attachFlexToContainerBatch(FlexController.FlexInContainerBatchParams params);

    void detachFlexFromContainer(String serialNumber);
    void detachFlexFromContainerBatch(List<String> validatedSerialNumbers);

    void detachFlexFromOrder(String serialNumber);
    void detachFlexFromOrderBatch(List<String> validatedSerialNumbers);

    List<FlexTO> getFlexListWrittenOff();


    //Write off, reserve, rename

    void writeOffFlex(String serialNum);

    void cancelWriteOffFlex(String serialNum);

    void reserveFlex(String serialNum);
    void unReserveFlex(String serialNum);
    void reserveFlexBatch(ArrayList<String> serialNums);
    void unReserveFlexBatch(ArrayList<String> serialNums);

    void setReservedFlexToOrder(String serialNum, String orderNum);

    void renameFlex(String oldSerialNum, String newSerialNum);

    Map<String, List<FlexTO>> flexListMountedToContainers(String orderNum);

    List<FlexHistoryTO> getFlexWarehouseMovement(String flexSerialNum);


    /********************* INNER USAGE METHODS *********************/

    //Flex
    List<Flex> getFlexListForWarehouse(String warehouseName);

    void updateFlex(Flex flex);

    //Orders

    FlexOrder getOrderByNumber(String orderNumber);

    List<FlexOrder> getOrdersByNumber(List<String> orderNumbers);

    FlexOrder addOrder(FlexOrder order);

    //Containers
    void upsertContainers(List<FlexContainer> containers);

    List<FlexContainer> getContainerListByOrder(String orderNum);

    FlexContainer getContainerByNumber(String containerNumber);

    List<FlexRenameTO> getRenamedFlexList();

    List<Flex> getBySerialNumbers(List<String> serialNumbers);


    //Statistic
    StatisticTO getStatistic();

    List<FlexWarehouse> getWarehouses();

    List<FlexOrderTO> getExportOrdersWithStatistic(String searchString);

    List<FlexOrderTO> getMountedOrdersWithStatistic(String searchString);

}
