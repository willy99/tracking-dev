package com.tmw.tracking.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Store;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by pzhelnov on 1/25/2017.
 */
@Entity
@Table(name="tr_order_details")
@JsonIgnoreProperties(ignoreUnknown = true)
@Embeddable
public class OrderDetails extends RootEntity {

    private static final long serialVersionUID = -6886848877574564547L;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="tracking_order", nullable=false, updatable = true)
    private Order order;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="driver", nullable=false, updatable = true)
    @IndexedEmbedded
    private Driver driver;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="container_type", nullable=false, updatable = true)
    @IndexedEmbedded
    private ContainerType containerType;

    @Column(nullable = true, name = "container_number")
    @Field(index= Index.YES, analyze= Analyze.NO, store= Store.NO)
    private String containerNumber;


    @Column(nullable = true, name = "weight")
    private Double weight;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public String getContainerNumber() {
        return containerNumber;
    }

    public void setContainerNumber(String number) {
        this.containerNumber = number;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public ContainerType getContainerType() {
        return containerType;
    }

    public void setContainerType(ContainerType containerType) {
        this.containerType = containerType;
    }
}
