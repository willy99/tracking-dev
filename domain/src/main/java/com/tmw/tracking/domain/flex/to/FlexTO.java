package com.tmw.tracking.domain.flex.to;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FlexTO {

    private String serialNumber;
    @JsonBackReference
    private FlexOrderTO exportOrder;
    @JsonBackReference
    private FlexContainerTO importContainer;
    @JsonBackReference
    private FlexContainerTO mountContainer;
    private String mountContainerNumber;
    private String importContainerNumber;
    private FlexWarehouseTO warehouse;
    private Date importDate;
    private Date exportDate;
    private Date writeOffDate;
    private Date mountDate;
    private Date updatedDate;
    private Long id;


    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public FlexOrderTO getExportOrder() {
        return exportOrder;
    }

    public void setExportOrder(FlexOrderTO exportOrder) {
        this.exportOrder = exportOrder;
    }

    public FlexContainerTO getImportContainer() {
        return importContainer;
    }

    public void setImportContainer(FlexContainerTO importContainer) {
        this.importContainer = importContainer;
    }

    public FlexContainerTO getMountContainer() {
        return mountContainer;
    }

    public void setMountContainer(FlexContainerTO mountContainer) {
        this.mountContainer = mountContainer;
    }

    public FlexWarehouseTO getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(FlexWarehouseTO warehouse) {
        this.warehouse = warehouse;
    }

    public Date getImportDate() {
        return importDate;
    }

    public void setImportDate(Date importDate) {
        this.importDate = importDate;
    }

    public Date getExportDate() {
        return exportDate;
    }

    public void setExportDate(Date exportDate) {
        this.exportDate = exportDate;
    }

    public Date getWriteOffDate() {
        return writeOffDate;
    }

    public void setWriteOffDate(Date writeOffDate) {
        this.writeOffDate = writeOffDate;
    }

    public Date getMountDate() {
        return mountDate;
    }

    public void setMountDate(Date mountDate) {
        this.mountDate = mountDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMountContainerNumber() {
        return mountContainerNumber;
    }

    public void setMountContainerNumber(String mountContainerNumber) {
        this.mountContainerNumber = mountContainerNumber;
    }

    public String getImportContainerNumber() {
        return importContainerNumber;
    }

    public void setImportContainerNumber(String importContainerNumber) {
        this.importContainerNumber = importContainerNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlexTO flexTO = (FlexTO) o;
        return serialNumber.equals(flexTO.serialNumber) &&
                Objects.equals(id, flexTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serialNumber, id);
    }
}
