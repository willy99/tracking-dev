package com.tmw.tracking.domain.containercalc.algorithm;

import com.tmw.tracking.domain.containercalc.entities.Cargo;
import com.tmw.tracking.domain.containercalc.entities.ContainerCalcResult;
import com.tmw.tracking.domain.containercalc.entities.PlacementCombination;
import com.tmw.tracking.domain.containercalc.entities.PlacementTypeResult;
import com.tmw.tracking.domain.containercalc.enums.PlacementType;
import com.tmw.tracking.entity.ContainerType;
import com.tmw.tracking.utils.DomainUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public abstract class ShapeAlgorithm {

    private final static Logger logger = LoggerFactory.getLogger(ShapeAlgorithm.class);

    protected final Cargo cargo;
    protected final ContainerType containerType;

    ShapeAlgorithm(Cargo cargo, ContainerType containerType) {
        this.cargo = cargo;
        this.containerType = containerType;
    }

    abstract List<PlacementCombination> calculatePlacementCombinations();

    abstract Double getSpecificLoadingVolume();

    public ContainerCalcResult calculate() {
        Double specificCargoVolume = getSpecificLoadingVolume(); //UPO
        Double specificContainerCapacity = containerType.getVolume() / (containerType.getWorkload() * 1000); //U
        logger.debug("Calculation for " + containerType.getName());
        logger.debug("---UPO(m3/tonn) = " + specificCargoVolume);
        int maximumCargoQuantity;

        if (specificCargoVolume < specificContainerCapacity) {
            maximumCargoQuantity = (int)Math.floor(containerType.getWorkload() / cargo.getWeight());
            logger.debug("---By container load capacity: " + maximumCargoQuantity);
        } else {
            maximumCargoQuantity = (int)Math.floor(containerType.getVolume() / cargo.getVolume());
            logger.debug("---By container volume: " + maximumCargoQuantity);
        }

        ContainerCalcResult calcResultForCargo =
                new ContainerCalcResult.ContainerCalcResultBuilder().
                        setBestCombination(getBestCombinationForContainer(calculatePlacementCombinations())).
                        setContainerType(containerType).build();
        if (specificCargoVolume < specificContainerCapacity) {
            //weight exceed
            calcResultForCargo.getBestCombination().maximumWeightExceed(maximumCargoQuantity);
        }

        Integer maxfor3d = DomainUtils.getConstantIntegerValue ("calculator_parameter_max_for_3d");
        if (calcResultForCargo.getBestCombination().getOverallQuantity() <= maxfor3d) { //do we need to draw
            calculatePlacementPositions(calcResultForCargo);
        }

        if (calcResultForCargo.getContainerType().getFreightRate() > 0) {
            calcResultForCargo.setCarryCost( new BigDecimal(calcResultForCargo.getContainerType().getFreightRate() * 1000 / (calcResultForCargo.getBestCombination().getOverallQuantity() * cargo.getWeight())).setScale(2, RoundingMode.HALF_UP).doubleValue());
        } else {
            calcResultForCargo.setCarryCost(0d);
        }

        calcResultForCargo.setCargo(cargo);

        return calcResultForCargo;

    }

    PlacementTypeResult createPlacementTypeResult(PlacementType placementType, int length, int width, int height) {
        return new PlacementTypeResult.PlacementTypeResultBuilder().
                setPlacementType(placementType).
                setLengthQuantity(length).
                setHeightQuantity(height).
                setWidthQuantity(width).
                build();
    }

    private PlacementCombination getBestCombinationForContainer(List<PlacementCombination> placementCombinations) {
        int maxOverallQuantity = 0;
        int index = 0;
        for (int i = 0; i < placementCombinations.size(); i++) {

            PlacementCombination combination = placementCombinations.get(i);

            if (combination.getOverallQuantity() > maxOverallQuantity) {
                maxOverallQuantity = combination.getOverallQuantity();
                index = i;
            }
        }
        return placementCombinations.get(index);
    }


    abstract void calculatePlacementPositions(ContainerCalcResult calcResult);

    Boolean isLayeringAvailable() {
        return cargo.isLayering();
    }
}
