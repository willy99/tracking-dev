package com.tmw.tracking.domain.flex.to;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tmw.tracking.domain.flex.entities.FlexStatusEnum;

import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FlexContainerTO {

    private String containerNumber;
    @JsonBackReference
    private FlexOrderTO order;
    private FlexStatusEnum status;
    private Integer flexQty;
    private Integer processedFlexQty;
    private Set<FlexTO> flexes = new HashSet<FlexTO>();
    private Date updatedDate;
    private Long id;

    public String getContainerNumber() {
        return containerNumber;
    }

    public void setContainerNumber(String containerNumber) {
        this.containerNumber = containerNumber;
    }

    public FlexOrderTO getOrder() {
        return order;
    }

    public void setOrder(FlexOrderTO order) {
        this.order = order;
    }

    public FlexStatusEnum getStatus() {
        return status;
    }

    public void setStatus(FlexStatusEnum status) {
        this.status = status;
    }

    public Integer getFlexQty() {
        return flexQty;
    }

    public void setFlexQty(Integer flexQty) {
        this.flexQty = flexQty;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Integer getProcessedFlexQty() {
        return processedFlexQty;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setProcessedFlexQty(Integer processedFlexQty) {
        this.processedFlexQty = processedFlexQty;
    }

    public Set<FlexTO> getFlexes() {
        return flexes;
    }

    public FlexTO getFlexByNum(String serialNum) {
        for (FlexTO flexTO: flexes) {
            if (flexTO.getSerialNumber().equals(serialNum)) {
                return flexTO;
            }
        }
        return null;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlexContainerTO that = (FlexContainerTO) o;
        return Objects.equals(containerNumber, that.containerNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(containerNumber);
    }
}
