package com.tmw.tracking.domain.flex.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tmw.tracking.entity.TenantSpecificEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name="flex_order")
@JsonIgnoreProperties(ignoreUnknown = true)
@Embeddable
public class FlexOrder extends TenantSpecificEntity {

    @NotNull(message = "Order Number can't be null")
    @Column(nullable = false, name = "order_number")
    private String orderNumber;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "execution_date")
    private Date executionDate;

    @Enumerated(EnumType.STRING)
    @Column(unique = false, nullable = false, name="order_type")
    private FlexOrderTypeEnum orderType;

    @Column(unique = false, nullable = false, name="export_container_qty")
    private Integer exportContainerQty;

    @Column(unique = false, nullable = false, name="export_flex_qty")
    private Integer exportFlexQty;

    @Enumerated(EnumType.STRING)
    @Column(unique = false, nullable = false, name="order_status")
    private FlexStatusEnum status;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "exportOrder")
    private Set<Flex> exportFlexes;

    /*@OneToMany(fetch = FetchType.EAGER, mappedBy = "importOrder")
    private List<FlexContainer> importContainers;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "exportOrder")
    private List<FlexContainer> mountContainers;*/

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Date getExecutionDate() {
        return executionDate;
    }

    public void setExecutionDate(Date executionDate) {
        this.executionDate = executionDate;
    }

    public FlexOrderTypeEnum getOrderType() {
        return orderType;
    }

    public void setOrderType(FlexOrderTypeEnum orderType) {
        this.orderType = orderType;
    }

    public Integer getExportContainerQty() {
        return exportContainerQty;
    }

    public void setExportContainerQty(Integer exportContainerQty) {
        this.exportContainerQty = exportContainerQty;
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

    public Set<Flex> getExportFlexes() {
        return exportFlexes;
    }

    public void setExportFlexes(Set<Flex> exportFlexes) {
        this.exportFlexes = exportFlexes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        FlexOrder flexOrder = (FlexOrder) o;
        return Objects.equals(orderNumber, flexOrder.orderNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderNumber);
    }
}
