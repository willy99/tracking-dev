package com.tmw.tracking.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.tmw.tracking.entity.enums.ContainerGroup;
import com.tmw.tracking.utils.FlexibleDoubleDeserializer;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by pzhelnov on 1/23/2017.
 */
@Entity
@Table(name="tr_container_type")
@JsonIgnoreProperties(ignoreUnknown = true)
@Embeddable
public class ContainerType extends RootEntity {
    private static final long serialVersionUID = -6886848877574564547L;


    @Enumerated(EnumType.STRING)
    @Column(unique = false, nullable = false, name="container_group")
    @NotNull(message = "type can't be null")
    private ContainerGroup containerGroup;


    @Column(unique = false, nullable = true, name = "name")
    private String name;

    @Column(nullable = true, name = "length")
    @JsonDeserialize(using = FlexibleDoubleDeserializer.class)
    private Double length;

    @Column(nullable = true, name = "width")
    @JsonDeserialize(using = FlexibleDoubleDeserializer.class)
    private Double width;

    @Column(nullable = true, name = "height")
    @JsonDeserialize(using = FlexibleDoubleDeserializer.class)
    private Double height;

    @Column(nullable = true, name = "workload")
    @JsonDeserialize(using = FlexibleDoubleDeserializer.class)
    private Double workload;

    @Column(nullable = true, name = "specific_tonnage")
    @JsonDeserialize(using = FlexibleDoubleDeserializer.class)
    private Double specificTonnage;

    @Column(nullable = true, name = "max_workload")
    @JsonDeserialize(using = FlexibleDoubleDeserializer.class)
    private Double maxWorkload;

    @javax.persistence.Transient
    @JsonDeserialize(using = FlexibleDoubleDeserializer.class)
    private Double freightRate = 0d;

    public ContainerType() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getWorkload() {
        return workload;
    }

    public void setWorkload(Double workload) {
        this.workload = workload;
    }

    public Double getSpecificTonnage() {
        return specificTonnage;
    }

    public void setSpecificTonnage(Double specificTonnage) {
        this.specificTonnage = specificTonnage;
    }

    public Double getMaxWorkload() {
        return maxWorkload;
    }

    public void setMaxWorkload(Double maxWorkload) {
        this.maxWorkload = maxWorkload;
    }

    public Double getFreightRate() {
        return freightRate;
    }

    public void setFreightRate(Double freightRate) {
        this.freightRate = freightRate;
    }

    public Double getVolume() {
        return getLength() * getWidth() * getHeight();
    }

    public ContainerGroup getContainerGroup() {
        return containerGroup;
    }

    public void setContainerGroup(ContainerGroup containerGroup) {
        this.containerGroup = containerGroup;
    }
}
