package com.tmw.tracking.domain.flex.to;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Filter DTO for the Flex Management search endpoint.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FlexSearchFilterTO {

    private String serialNum;
    private String containerNum;
    private String exportOrderNum;
    private String warehouseName;

    /** If true — only flexes that have an exportOrder attached. */
    private Boolean hasExportOrder;

    /** If true — only flexes that are mounted to a container. */
    private Boolean hasMountContainer;

    /** If true — only flexes still "on balance": no export order and not mounted to a container. */
    private Boolean onBalance;

    public String getSerialNum() { return serialNum; }
    public void setSerialNum(String serialNum) { this.serialNum = serialNum; }

    public String getContainerNum() { return containerNum; }
    public void setContainerNum(String containerNum) { this.containerNum = containerNum; }

    public String getExportOrderNum() { return exportOrderNum; }
    public void setExportOrderNum(String exportOrderNum) { this.exportOrderNum = exportOrderNum; }

    public String getWarehouseName() { return warehouseName; }
    public void setWarehouseName(String warehouseName) { this.warehouseName = warehouseName; }

    public Boolean getHasExportOrder() { return hasExportOrder; }
    public void setHasExportOrder(Boolean hasExportOrder) { this.hasExportOrder = hasExportOrder; }

    public Boolean getHasMountContainer() { return hasMountContainer; }
    public void setHasMountContainer(Boolean hasMountContainer) { this.hasMountContainer = hasMountContainer; }

    public Boolean getOnBalance() { return onBalance; }
    public void setOnBalance(Boolean onBalance) { this.onBalance = onBalance; }
}
