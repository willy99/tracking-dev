package com.tmw.tracking.service.impl;

import com.tmw.tracking.dao.ContainerTypeDao;
import com.tmw.tracking.domain.containercalc.algorithm.ShapeAlgorithm;
import com.tmw.tracking.domain.containercalc.algorithm.ShapeAlgorithmFactory;
import com.tmw.tracking.domain.containercalc.entities.Cargo;
import com.tmw.tracking.domain.containercalc.entities.ContainerCalcResult;
import com.tmw.tracking.domain.containercalc.entities.PackageType;
import com.tmw.tracking.domain.containercalc.entities.PlacementTypeResult;
import com.tmw.tracking.domain.containercalc.enums.CargoType;
import com.tmw.tracking.domain.containercalc.enums.ShapeType;
import com.tmw.tracking.entity.ContainerType;
import com.tmw.tracking.entity.enums.ContainerGroup;
import com.tmw.tracking.service.ContainerCalcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

public class ContainerCalcServiceImpl implements ContainerCalcService {

    private final static Logger logger = LoggerFactory.getLogger(ContainerCalcServiceImpl.class);
    private final ContainerTypeDao containerTypeDao;
    private Collection<ContainerType> containerTypes;
    private Collection<PackageType> packageTypes;

    @Inject
    public ContainerCalcServiceImpl(
            final ContainerTypeDao containerTypeDao) {
        this.containerTypeDao = containerTypeDao;
    }
    @Override
    public Collection<ContainerCalcResult> retrieveCalculation(Cargo cargo, Collection<ContainerType> containerTypes) {
        logger.debug("Cargo: " + cargo.getName() + " L = " + cargo.getLength() + " W = "+cargo.getWidth() + " H = " + cargo.getHeight() + " Weight = " + cargo.getWeight() );
        return calculateResult(cargo, containerTypes);
    }

    private Collection<ContainerCalcResult> calculateResult(Cargo cargo, Collection<ContainerType> containerTypes) {

        Collection<ContainerCalcResult> calcResults = new ArrayList<ContainerCalcResult>();

        for (ContainerType containerType: containerTypes) {

            ShapeAlgorithm algorithm = ShapeAlgorithmFactory.createAlgorithm(cargo, containerType);

            ContainerCalcResult calcResultForCargo = algorithm.calculate();

            logger.debug("---Optimal quantity for container :" + containerType.getName() + " = " + calcResultForCargo.getBestCombination().getOverallQuantity());
            for (PlacementTypeResult placementType: calcResultForCargo.getBestCombination().getPlacementTypeResults().values()) {
                logger.debug("---- combination type " + placementType.getPlacementType());
                logger.debug("-------By width:" + placementType.getWidthQuantity());
                logger.debug("-------By length:" + placementType.getLengthQuantity());
                logger.debug("-------By height:" + placementType.getHeightQuantity());
            }
            calcResults.add(calcResultForCargo);

        }

        ContainerCalcResult optimal = selectOptimalResult(calcResults);
        if (optimal != null) {
            optimal.setOptimal(true);
        }

        return calcResults;
    }

    private ContainerCalcResult selectOptimalResult(Collection<ContainerCalcResult> calcResults) {
        return calcResults.stream()
                .filter(n -> n.getCarryCost() > 0d)
                .min(Comparator.comparing(ContainerCalcResult::getCarryCost))
                .orElse(null);
    }


    /**
     * refactor later to the entity collection
     */
    public Collection<ContainerType> getContainerTypes() {
        if (containerTypes == null) {
            containerTypes = containerTypeDao.getAll();
        }
        return containerTypes;
    }

    @Override
    public ContainerType getByName(String name) {
        return getContainerTypes().stream()
                .filter(type -> type.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    @Override
    public ContainerType getByNameAndContainerGroup(String name, ContainerGroup containerGroup) {
        return getContainerTypes().stream()
                .filter(type -> type.getName().equalsIgnoreCase(name) && type.getContainerGroup().equals(containerGroup))
                .findFirst()
                .orElse(null);
    }


    /**
     * refactor later to the entity collection
     */
    public Collection<PackageType> getPackagetTypes() {
        if (packageTypes != null) { return packageTypes; }
        packageTypes = new ArrayList<PackageType>();
        packageTypes.add(new PackageType.PackageTypeBuilder().
                setCargoType(CargoType.BARREL).setPackingFactor(1.02d).setShapeType(ShapeType.CYLINDER).
                setDefaultSideLaying(false).setDefaultLayering(true).
                build());
        packageTypes.add(new PackageType.PackageTypeBuilder().
                setCargoType(CargoType.SACK).setPackingFactor(0.8).setShapeType(ShapeType.BOX).
                setDefaultSideLaying(false).setDefaultLayering(true).
                build());
        packageTypes.add(new PackageType.PackageTypeBuilder().
                setCargoType(CargoType.BIGBAG).setPackingFactor(1.1).setShapeType(ShapeType.BOX).
                setDefaultSideLaying(false).setDefaultLayering(false).
                build());
        packageTypes.add(new PackageType.PackageTypeBuilder().
                setCargoType(CargoType.BOX).setPackingFactor(1.02).setShapeType(ShapeType.BOX).
                setDefaultSideLaying(true).setDefaultLayering(true).
                build());
        packageTypes.add(new PackageType.PackageTypeBuilder().
                setCargoType(CargoType.CASE).setPackingFactor(1.05).setShapeType(ShapeType.BOX).
                setDefaultSideLaying(true).setDefaultLayering(true).
                build());
        packageTypes.add(new PackageType.PackageTypeBuilder().
                setCargoType(CargoType.PALLET).setPackingFactor(1.02).setShapeType(ShapeType.BOX).
                setDefaultSideLaying(false).setDefaultLayering(false).
                build());
        packageTypes.add(new PackageType.PackageTypeBuilder().
                setCargoType(CargoType.PIPES).setPackingFactor(1.02).setShapeType(ShapeType.PIPE).
                setDefaultSideLaying(false).setDefaultLayering(true).
                build());
        return packageTypes;
    }

}
