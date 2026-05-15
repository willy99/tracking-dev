package com.tmw.tracking.domain.flex.dao;

import com.tmw.tracking.domain.flex.entities.*;
import com.tmw.tracking.domain.flex.to.FlexTO;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface FlexDao {

    Flex add(Flex flex);

    void updateBatch(List<Flex> flexList, List<Flex> toUpdate);

    Flex update(Flex flex);

    Flex getBySerialNumber(String serialNumber);

    List<Flex> getAllByWarehouse(String warehouseName);

    List<FlexTO> getFlexTOListByWarehouse(String warehouseName);

    Integer getImportedFlexCountForContainer(FlexContainer container);

    Integer getExportedFlexCountForOrder(FlexOrder order);

    Integer getMountedFlexCountForOrder(FlexOrder order);

    List<Flex> getFlexListForImportedOrders(Date lastUpdated, FlexStatusEnum orderStatus);

    List<Flex> getFlexListForMountedOrders(Date lastUpdated);

    List<FlexTO> getExportFlexes(Date lastUpdated, String orderNum);

    List<FlexTO> getMountedFlexes(Date lastUpdated, String orderNum);

    List<FlexTO> getFlexesForOrders(List<FlexOrder> orders);

    List<FlexTO> getReservedFlexes();

    List<FlexTO> getBaseFlexes();

    void removeFlex(Flex flex);
    void removeFlexBatch(Set<Flex> flexesToRemove);


    //FLEX RENAME
    List<FlexRename> getRenamedFlexList();

    List<String> getRemovedFlexList();

    void updateFlexRename(FlexRename flexRename)  ;

    List<Flex> getBySerialNumbers(List<String> serialNumbers);

    List<Flex> getFlexListForExportOrderByNums(List<String> flexOrderNums);

    //Statistic
    List<FlexTO> getImportedFlexes(Date lastUpdated, String containerNum);

}
