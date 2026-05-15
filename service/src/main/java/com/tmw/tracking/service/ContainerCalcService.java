package com.tmw.tracking.service;

import com.tmw.tracking.domain.containercalc.entities.Cargo;
import com.tmw.tracking.domain.containercalc.entities.ContainerCalcResult;
import com.tmw.tracking.domain.containercalc.entities.PackageType;
import com.tmw.tracking.entity.ContainerType;
import com.tmw.tracking.entity.enums.ContainerGroup;

import java.util.Collection;

public interface ContainerCalcService {

    Collection<ContainerCalcResult> retrieveCalculation(Cargo cargo, Collection<ContainerType> containerTypes);

    Collection<ContainerType> getContainerTypes();

    ContainerType getByName(String name);

    ContainerType getByNameAndContainerGroup(String name, ContainerGroup containerGroup);

    Collection<PackageType> getPackagetTypes();
}
