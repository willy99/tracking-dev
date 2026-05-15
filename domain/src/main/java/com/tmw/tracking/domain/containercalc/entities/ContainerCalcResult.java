package com.tmw.tracking.domain.containercalc.entities;

import com.tmw.tracking.domain.containercalc.enums.LoadingType;
import com.tmw.tracking.entity.ContainerType;

public class ContainerCalcResult {

    private PlacementCombination bestCombination;

    private ContainerType containerType;

    private Cargo cargo;
    private Double carryCost;
    private Boolean optimal;

    private ContainerCalcResult(ContainerCalcResultBuilder builder) {
        this.containerType = builder.containerType;
        this.bestCombination = builder.bestCombination;
    }


    public LoadingType getLoadingType() { return bestCombination.getLoadingType(); }

    public ContainerType getContainerType() {
        return containerType;
    }
    
    public Cargo getCargo() {
        return cargo;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }

    public Double getCarryCost() {
        return carryCost;
    }

    public void setCarryCost(Double carryCost) {
        this.carryCost = carryCost;
    }

    public Boolean getOptimal() {
        return optimal;
    }

    public void setOptimal(Boolean optimal) {
        this.optimal = optimal;
    }

    public PlacementCombination getBestCombination() {
        return bestCombination;
    }

    public static class ContainerCalcResultBuilder {

        private ContainerType containerType;
        private PlacementCombination bestCombination;

        public ContainerCalcResult build() {
            if (containerType == null || bestCombination == null){
                throw new RuntimeException("Not all parameters set");
            }
            return new ContainerCalcResult(this);
        }

        public ContainerCalcResultBuilder setBestCombination(PlacementCombination placementCombination) {
            this.bestCombination = placementCombination;
            return this;
        }

        public ContainerCalcResultBuilder setContainerType(ContainerType containerType) {
            this.containerType = containerType;
            return this;
        }



    }

}
