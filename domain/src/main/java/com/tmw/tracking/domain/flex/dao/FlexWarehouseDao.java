package com.tmw.tracking.domain.flex.dao;

import com.tmw.tracking.domain.flex.entities.FlexWarehouse;

import java.util.List;

public interface FlexWarehouseDao {

    String BASE_WAREHOUSE = "Local Warehouse";
    String WRITE_OFF_WAREHOUSE = "Write-Off warehouse";
    String RESERVE_WAREHOUSE = "Reserve warehouse";

    FlexWarehouse add(FlexWarehouse warehouse);

    FlexWarehouse update(FlexWarehouse warehouse);

    List<FlexWarehouse> getAllWarehouses();

    FlexWarehouse getWarehouseByName(String name);

    FlexWarehouse getBaseWarehouse();

    FlexWarehouse getWriteOffWarehouse();

    FlexWarehouse getReserveWarehouse();

}
