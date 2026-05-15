package com.tmw.tracking.domain.containercalc.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Dimension {

    private Double length;
    private Double width;
    private Double height;

    private Dimension() {

    }

    public Dimension(Double length, Double width, Double height) {
        this.length = length;
        this.width = width;
        this.height = height;
    }

    public Double getLength() {
        return length;
    }

    public Double getWidth() {
        return width;
    }

    public Double getHeight() {
        return height;
    }

    public Double getDiameter() {
        return width;
    }
}
