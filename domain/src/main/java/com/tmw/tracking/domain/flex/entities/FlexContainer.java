package com.tmw.tracking.domain.flex.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tmw.tracking.entity.TenantSpecificEntity;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name="flex_container")
@JsonIgnoreProperties(ignoreUnknown = true)
@Embeddable
public class FlexContainer extends TenantSpecificEntity {

    @NotNull(message = "Container Number can't be null")
    @Column(nullable = false, name = "container_number")
    @Field(index= Index.YES, analyze= Analyze.YES, store= Store.NO)
    private String containerNumber;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="flex_import_order", nullable=false, updatable = true)
    private FlexOrder importOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="flex_export_order", nullable=false, updatable = true)
    private FlexOrder exportOrder;

    @Enumerated(EnumType.STRING)
    @Column(unique = false, nullable = false, name="container_status")
    private FlexStatusEnum status;

    @Column(unique = false, nullable = false, name="import_flex_qty")
    private Integer importFlexQty;

    @Column(unique = false, nullable = false, name="export_flex_qty")
    private Integer exportFlexQty;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "importContainer")
    private Set<Flex> importFlexes;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "mountContainer")
    private Set<Flex> mountFlexes;

    public String getContainerNumber() {
        return containerNumber;
    }

    public void setContainerNumber(String containerNumber) {
        this.containerNumber = containerNumber;
    }

    public FlexOrder getExportOrder() {
        return exportOrder;
    }

    public void setExportOrder(FlexOrder exportOrder) {
        this.exportOrder = exportOrder;
    }

    public FlexOrder getImportOrder() {
        return importOrder;
    }

    public void setImportOrder(FlexOrder importOrder) {
        this.importOrder = importOrder;
    }

    public Integer getImportFlexQty() {
        return importFlexQty;
    }

    public void setImportFlexQty(Integer importFlexQty) {
        this.importFlexQty = importFlexQty;
    }

    public Integer getExportFlexQty() {
        return exportFlexQty;
    }

    public void setExportFlexQty(Integer exportFlexQty) {
        this.exportFlexQty = exportFlexQty;
    }

    public FlexStatusEnum getStatus() {
        return status;
    }

    public void setStatus(FlexStatusEnum status) {
        this.status = status;
    }

    public Set<Flex> getImportFlexes() {
        return importFlexes;
    }

    public void setImportFlexes(Set<Flex> importFlexes) {
        this.importFlexes = importFlexes;
    }

    public Set<Flex> getMountFlexes() {
        return mountFlexes;
    }

    public void setMountFlexes(Set<Flex> mountFlexes) {
        this.mountFlexes = mountFlexes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        FlexContainer that = (FlexContainer) o;
        return Objects.equals(containerNumber, that.containerNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(containerNumber);
    }
}
