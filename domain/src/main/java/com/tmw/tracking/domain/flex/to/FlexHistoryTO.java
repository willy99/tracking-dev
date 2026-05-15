package com.tmw.tracking.domain.flex.to;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tmw.tracking.domain.flex.entities.FlexWarehouseTypeEnum;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FlexHistoryTO {
    private String flex;
    private String warehouseName;
    private FlexWarehouseTypeEnum warehouseType;
    private Date date;

    public String getFlex() {
        return flex;
    }

    public void setFlex(String flex) {
        this.flex = flex;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public FlexWarehouseTypeEnum getWarehouseType() {
        return warehouseType;
    }

    public void setWarehouseType(FlexWarehouseTypeEnum warehouseType) {
        this.warehouseType = warehouseType;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}

