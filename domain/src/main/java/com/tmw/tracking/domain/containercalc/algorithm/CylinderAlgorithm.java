package com.tmw.tracking.domain.containercalc.algorithm;

import com.tmw.tracking.domain.containercalc.entities.*;
import com.tmw.tracking.domain.containercalc.enums.ContainerPosition;
import com.tmw.tracking.domain.containercalc.enums.PlacementType;
import com.tmw.tracking.entity.ContainerType;
import java.util.ArrayList;
import java.util.List;

public class CylinderAlgorithm extends ShapeAlgorithm {

    CylinderAlgorithm(Cargo cargo, ContainerType containerType) {
        super(cargo, containerType);
    }

    @Override
    List<PlacementCombination> calculatePlacementCombinations() {
        List<PlacementCombination> placementTypeCombinations = new ArrayList<>();
        final Double K = cargo.getPackageType().getPackingFactor();

        PlacementCombination combinationRow = new PlacementCombination();
        combinationRow.getPlacementTypeResults().put(ContainerPosition.A_MAIN, createPlacementTypeResult(PlacementType.ROW,
                (int)Math.floor(containerType.getLength() / (cargo.getDiameter() * K)),
                (int)Math.floor(containerType.getWidth() / (cargo.getDiameter() * K)),
                isLayeringAvailable() ? (int)Math.floor(containerType.getHeight() / (cargo.getHeight() * K)):
                        (cargo.getHeight() > containerType.getHeight()? 0: 1) ));


        //FOR WIDTH ALIGN
        Integer widthQuantity = (int)Math.floor(containerType.getWidth() / (cargo.getDiameter() * K));
        Double freeLegde = Math.abs((containerType.getWidth() - widthQuantity * cargo.getDiameter() * K)); //X
        Double biasCatheter = (freeLegde < (cargo.getDiameter() * K) / 2) ? //A
                freeLegde:
                cargo.getDiameter() * K / 2;

        //Z = √((diameter)^2-А^2 )
        Double nextRowWidth = Math.sqrt( Math.pow(cargo.getDiameter(), 2 ) - Math.pow(biasCatheter, 2));
        //Ul = (Lконт - (diameter*К))/(Z*K) + 1

        PlacementTypeResult rowWidthAlign = createPlacementTypeResult(PlacementType.ROW_WIDTH_ALIGN,
                (int)Math.floor( ((containerType.getLength() - (cargo.getDiameter() * K)) / (nextRowWidth * K))) + 1,
                (int)Math.floor(widthQuantity),
                isLayeringAvailable() ? (int)Math.floor(containerType.getHeight() / (cargo.getHeight() * K)) : 1);
        rowWidthAlign.setNextRowWidth(nextRowWidth);
        rowWidthAlign.setBiasCatheter(biasCatheter);
        rowWidthAlign.setFreeLedge(freeLegde);
        PlacementCombination combinationRowAlign = new PlacementCombination();
        combinationRowAlign.getPlacementTypeResults().put(ContainerPosition.A_MAIN, rowWidthAlign);


        //FOR LENGTH ALIGN
        Double lengthQuantity = containerType.getLength() / (cargo.getDiameter() * K);
        //A = (diameter*К)/2,

        freeLegde = Math.abs((containerType.getLength() - lengthQuantity * cargo.getDiameter() * K)); //X
        biasCatheter = (freeLegde < (cargo.getDiameter() * K) / 2) ? //A
                freeLegde:
                cargo.getDiameter() * K / 2;

        //biasCatheter = (cargo.getDiameter() * K ) / 2;
        //Z = √((diameter)^2-А^2 )
        nextRowWidth = Math.sqrt( Math.pow(cargo.getDiameter(), 2) - Math.pow(biasCatheter, 2) );

        PlacementTypeResult rowLengthAlign = createPlacementTypeResult(PlacementType.ROW_LENGTH_ALIGN,
                (int)Math.floor( lengthQuantity ),
                (int)Math.floor( ((containerType.getWidth() - (cargo.getDiameter() * K)) / (nextRowWidth * K))) + 1,
                isLayeringAvailable() ? (int)Math.floor( containerType.getHeight() / (cargo.getHeight() * cargo.getPackageType().getPackingFactor())): 1);
        rowLengthAlign.setNextRowWidth(nextRowWidth);
        rowLengthAlign.setBiasCatheter(biasCatheter);
        rowLengthAlign.setFreeLedge(freeLegde);

        PlacementCombination combinationLengthAlign = new PlacementCombination();
        combinationLengthAlign.getPlacementTypeResults().put(ContainerPosition.A_MAIN, rowLengthAlign);

        placementTypeCombinations.add(combinationRow);
        placementTypeCombinations.add(combinationRowAlign);
        placementTypeCombinations.add(combinationLengthAlign);

        return placementTypeCombinations;
    }

    @Override
    Double getSpecificLoadingVolume() {
        return cargo.getDiameter() * cargo.getDiameter() * cargo.getHeight() / (cargo.getWeight()) / 1000;
    }


    void calculatePlacementPositions(ContainerCalcResult calcResult) {

        PlacementTypeResult placementTypeResult = calcResult.getBestCombination().getPlacementTypeResults().get(ContainerPosition.A_MAIN); //Cylinder = only one placement type
        PlacementType placementType = placementTypeResult.getPlacementType();

        List<CargoPosition> cargoPositions = new ArrayList<>();
        final Dimension distortedCargoDimension = cargo.getDistortedCargoSize(placementType);
        placementTypeResult.setCargoDimension(distortedCargoDimension);

        final Double K = cargo.getPackageType().getPackingFactor();

        Double diameter = distortedCargoDimension.getDiameter();
        Double length = distortedCargoDimension.getLength();
        Double width = distortedCargoDimension.getWidth();

        Double height = distortedCargoDimension.getHeight();
        if (placementType == PlacementType.ROW_LENGTH_ALIGN) {
            width = placementTypeResult.getNextRowWidth();
        }
        if (placementType == PlacementType.ROW_WIDTH_ALIGN) {
            length = placementTypeResult.getNextRowWidth();
        }

        int offsetx = (int)Math.floor((((placementTypeResult.getLengthQuantity() * length / 10) - (placementTypeResult.getPlacementType() == PlacementType.ROW_LENGTH_ALIGN?placementTypeResult.getNextRowWidth() /10 /2:0) ) / 2 - length / 10 / 2 ) * K);
        int offsetz = (int)Math.floor( (placementTypeResult.getBiasCatheter() == null ? 0:
                (containerType.getWidth() - (cargo.getDiameter() * placementTypeResult.getWidthQuantity() + placementTypeResult.getBiasCatheter())) / 10 / 2)
                + (((placementTypeResult.getWidthQuantity() * width / 10)  - (placementTypeResult.getPlacementType() == PlacementType.ROW_WIDTH_ALIGN?placementTypeResult.getFreeLedge() /10 :0) ) / 2 - width / 10 / 2 ) * K);
        int offsety = (int)Math.floor(containerType.getHeight() / 10 / 2 - distortedCargoDimension.getHeight() / 10 / 2);

        Integer quantity = 0;
        Integer maxx = 0;

        int finalOffsetX = offsetx;
        int finalOffsetZ = offsetz;
        int finalOffsetY = offsety;


        for (Integer y = 0; y < placementTypeResult.getHeightQuantity(); y += 1) {
            List<CargoPosition> layer = new ArrayList<>();

            for (Integer x = 0; x < placementTypeResult.getLengthQuantity(); x += 1) {

                for (Integer z = 0; z < placementTypeResult.getWidthQuantity(); z += 1) {

                    if (placementType == PlacementType.ROW_LENGTH_ALIGN) {
                        finalOffsetX = offsetx + (((z +1) % 2 == 0)? ((int)Math.floor(placementTypeResult.getBiasCatheter()) /10) :0);
                    }
                    if (placementType == PlacementType.ROW_WIDTH_ALIGN) {
                        finalOffsetZ = offsetz + (((x +1) % 2 == 0)? ((int)Math.floor(placementTypeResult.getBiasCatheter()) /10) :0);
                    }

                    int posx = (int) Math.floor(x * (length / 10 * K) - finalOffsetX);
                    int posz = (int) Math.floor(z * (width / 10 * K) - finalOffsetZ);
                    int posy = (int) Math.floor(y * (height / 10 * 1.05) - finalOffsetY);
                    if (quantity < calcResult.getBestCombination().getOverallQuantity()) {
                        CargoPosition cargoPosition = new CargoPosition(posx, posy, posz);
                        cargoPositions.add(cargoPosition);
                        layer.add(cargoPosition);

                        quantity++;
                        maxx = x;

                    }
                }
            }

            if (placementTypeResult.getLengthQuantity() * placementTypeResult.getWidthQuantity() > layer.size()) {
                Integer centeringOffsetX = (int) Math.floor(containerType.getLength() / 10 / 2 - ( (maxx+1) * length) / 10 /2 -
                            ((containerType.getLength() - placementTypeResult.getLengthQuantity() * length) / 10 / 2 )) ;


                for (CargoPosition position : layer) {
                    position.setX(position.getX() + centeringOffsetX);
                }
            }
        }

        placementTypeResult.setCargoPositions(cargoPositions);

        //calcResult.getPlacementTypes().put(placementTypeResult.getPlacementType(), placementTypeResult);
    }

}
