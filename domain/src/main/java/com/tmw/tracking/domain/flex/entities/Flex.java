package com.tmw.tracking.domain.flex.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tmw.tracking.entity.TenantSpecificEntity;
import org.hibernate.annotations.Type;
import org.hibernate.search.annotations.IndexedEmbedded;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name="flex")
@JsonIgnoreProperties(ignoreUnknown = true)
@Embeddable
public class Flex extends TenantSpecificEntity {

    @NotNull(message = "SerialNumber can't be null")
    @Column(nullable = false, name = "serial_number")
    private String serialNumber;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="export_order", nullable=false, updatable = true)
    @IndexedEmbedded
    private FlexOrder exportOrder;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="import_container", nullable=false, updatable = true)
    @IndexedEmbedded
    private FlexContainer importContainer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="mount_container", nullable=false, updatable = true)
    @IndexedEmbedded
    private FlexContainer mountContainer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="warehouse", nullable=false, updatable = true)
    @IndexedEmbedded
    private FlexWarehouse warehouse;

    @Type(type = "yes_no")
    @Column(nullable = false, name = "deleted")
    private boolean deleted = false;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "import_date")
    private Date importDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "export_date")
    private Date exportDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "write_off_date")
    private Date writeOffDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "mount_date")
    private Date mountDate;

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public FlexOrder getExportOrder() {
        return exportOrder;
    }

    public void setExportOrder(FlexOrder exportOrder) {
        this.exportOrder = exportOrder;
    }

    public FlexContainer getImportContainer() {
        return importContainer;
    }

    public void setImportContainer(FlexContainer importContainer) {
        this.importContainer = importContainer;
    }

    public FlexWarehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(FlexWarehouse warehouse) {
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

    public FlexContainer getMountContainer() {
        return mountContainer;
    }

    public void setMountContainer(FlexContainer mountContainer) {
        this.mountContainer = mountContainer;
    }

    public void setMountDate(Date mountDate) {
        this.mountDate = mountDate;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Flex flex = (Flex) o;
        return Objects.equals(serialNumber, flex.serialNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serialNumber);
    }
}
