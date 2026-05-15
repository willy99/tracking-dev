package com.tmw.tracking.domain.containercalc.entities;

import com.tmw.tracking.domain.containercalc.enums.PlacementType;

import java.util.List;

public class PlacementTypeResult {

    private PlacementType placementType;
    //private ContainerPosition containerPosition;
    private Integer lengthQuantity;
    private Integer widthQuantity;
    private Integer heightQuantity;
    private Double nextRowWidth = 0d;
    private Double biasCatheter;
    private Double freeLedge;
    private Double freeClearance;

    private List<CargoPosition> cargoPositions;
    private Dimension cargoDimension;


    private Cargo distortedCargo;


    private PlacementTypeResult(PlacementTypeResultBuilder builder) {
        this.lengthQuantity = builder.lengthQuantity;
        this.widthQuantity = builder.widthQuantity;
        this.heightQuantity = builder.heightQuantity;
        this.placementType = builder.placementType;
    }

    public Integer getTotalQuantityForPlacementType() {
        return getLengthQuantity() * getWidthQuantity() * getHeightQuantity();
    }

    public Double getNextRowWidth() {
        return nextRowWidth;
    }

    public void setNextRowWidth(Double nextRowWidth) {
        this.nextRowWidth = nextRowWidth;
    }

    public Double getBiasCatheter() {
        return biasCatheter;
    }

    public void setBiasCatheter(Double biasCatheter) {
        this.biasCatheter = biasCatheter;
    }

    public Integer getLengthQuantity() {
        return lengthQuantity;
    }

    public Integer getWidthQuantity() {
        return widthQuantity;
    }

    public Integer getHeightQuantity() {
        return heightQuantity;
    }

    public PlacementType getPlacementType() {
        return placementType;
    }

    public List<CargoPosition> getCargoPositions() {
        return cargoPositions;
    }

    public void setCargoPositions(List<CargoPosition> cargoPositions) {
        this.cargoPositions = cargoPositions;
    }


    public Double getFreeLedge() {
        return freeLedge;
    }

    public void setFreeLedge(Double freeLedge) {
        this.freeLedge = freeLedge;
    }

    public Double getFreeClearance() {
        return freeClearance;
    }

    public void setFreeClearance(Double freeClearance) {
        this.freeClearance = freeClearance;
    }

    public Dimension getCargoDimension() {
        return cargoDimension;
    }

    public void setCargoDimension(Dimension cargoDimension) {
        this.cargoDimension = cargoDimension;
    }

    void setHeightQuantity(Integer heightQuantity) {
        this.heightQuantity = heightQuantity;
    }

    /*public ContainerPosition getContainerPosition() {
        return containerPosition;
    }

    public void setContainerPosition(ContainerPosition containerPosition) {
        this.containerPosition = containerPosition;
    }*/

    public static class PlacementTypeResultBuilder {

        private PlacementType placementType;
        private Integer lengthQuantity;
        private Integer widthQuantity;
        private Integer heightQuantity;


        public PlacementTypeResult build() {
            if (lengthQuantity == null || widthQuantity == null || heightQuantity == null || placementType == null){
                throw new RuntimeException("Not all parameters set");
            }
            return new PlacementTypeResult(this);
        }

        public PlacementTypeResult.PlacementTypeResultBuilder setLengthQuantity(Integer lengthQuantity) {
            this.lengthQuantity = lengthQuantity;
            return this;
        }

        public PlacementTypeResult.PlacementTypeResultBuilder setWidthQuantity(Integer widthQuantity) {
            this.widthQuantity = widthQuantity;
            return this;
        }

        public PlacementTypeResult.PlacementTypeResultBuilder setHeightQuantity(Integer heightQuantity) {
            this.heightQuantity = heightQuantity;
            return this;
        }

        public PlacementTypeResult.PlacementTypeResultBuilder setPlacementType(PlacementType placementType) {
            this.placementType = placementType;
            return this;
        }


    }


}
