package com.tmw.tracking.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by pzhelnov on 1/25/2017.
 */
@Entity
@Table(name="tr_container_location")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContainerLocation extends RootEntity {

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="detail", nullable=false, updatable = true)
    private OrderDetails orderDetails;

    @Column(nullable = false, name = "map_latitude")
    private Double latitude;

    @Column(nullable = false, name = "map_longitude")
    private Double longitude;

    @Column(nullable = true, name = "location_date")
    private Date locationDate;

    public OrderDetails getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(OrderDetails orderDetails) {
        this.orderDetails = orderDetails;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Date getLocationDate() {
        return locationDate;
    }

    public void setLocationDate(Date locationDate) {
        this.locationDate = locationDate;
    }
}
