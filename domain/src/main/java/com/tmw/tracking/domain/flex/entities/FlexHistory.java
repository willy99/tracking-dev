package com.tmw.tracking.domain.flex.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tmw.tracking.entity.TenantSpecificEntity;
import org.hibernate.search.annotations.IndexedEmbedded;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="flex_history")
@JsonIgnoreProperties(ignoreUnknown = true)
@Embeddable
public class FlexHistory extends TenantSpecificEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="flex", nullable=false, updatable = true)
    @IndexedEmbedded
    private Flex flex;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="warehouse", nullable=false, updatable = true)
    @IndexedEmbedded
    private FlexWarehouse warehouse;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "action_date")
    private Date actionDate;

    public Flex getFlex() {
        return flex;
    }

    public void setFlex(Flex flex) {
        this.flex = flex;
    }

    public FlexWarehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(FlexWarehouse warehouse) {
        this.warehouse = warehouse;
    }

    public Date getActionDate() {
        return actionDate;
    }

    public void setActionDate(Date actionDate) {
        this.actionDate = actionDate;
    }
}
