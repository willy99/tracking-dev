package com.tmw.tracking.domain.containercalc.entities;

import com.tmw.tracking.domain.containercalc.entities.Cargo;
import com.tmw.tracking.entity.ContainerType;

import java.util.List;

public class ContainerCalcRequest {

    Cargo cargo;
    List<ContainerType> containerTypes;

    public ContainerCalcRequest() {

    }

    public ContainerCalcRequest(Cargo cargo, List<ContainerType> containerTypes) {
        this.cargo = cargo;
        this.containerTypes = containerTypes;
    }

    public Cargo getCargo() {
        return cargo;
    }


    public List<ContainerType> getContainerTypes() {
        return containerTypes;
    }


}
