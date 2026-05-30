package com.tmw.tracking.domain.flex.to;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tmw.tracking.domain.flex.entities.FlexOrderTypeEnum;
import com.tmw.tracking.domain.flex.entities.FlexStatusEnum;

import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FlexOrderTO {

    private String orderNumber;
    private Date executionDate;
    private FlexOrderTypeEnum orderType;
    private FlexStatusEnum status;
    private Set<FlexContainerTO> containers = new HashSet<>();
    private Date updatedDate;
    private Date createdDate;
    private Integer flexQty; //export flex qty expected
    private Integer processedFlexQty; //export flex qty actual
    private Long id;

    private Set<FlexTO> writtenOffFlexes = new HashSet<FlexTO>();

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

    public FlexStatusEnum getStatus() {
        return status;
    }

    public void setStatus(FlexStatusEnum status) {
        this.status = status;
    }

    public Set<FlexContainerTO> getContainers() {
        return containers;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Set<FlexTO> getWrittenOffFlexes() {
        return writtenOffFlexes;
    }

    public Integer getFlexQty() {
        return flexQty;
    }

    public void setFlexQty(Integer flexQty) {
        this.flexQty = flexQty;
    }

    public Integer getProcessedFlexQty() {
        return processedFlexQty;
    }

    public void setProcessedFlexQty(Integer processedFlexQty) {
        this.processedFlexQty = processedFlexQty;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FlexContainerTO getContainerByNum(String containerNum) {
        for (FlexContainerTO flexContainerTO: containers) {
            if (flexContainerTO.getContainerNumber().equals(containerNum)) {
                return flexContainerTO;
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlexOrderTO that = (FlexOrderTO) o;
        return Objects.equals(orderNumber, that.orderNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderNumber);
    }
}
