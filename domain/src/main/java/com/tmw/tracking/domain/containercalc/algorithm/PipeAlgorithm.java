package com.tmw.tracking.domain.containercalc.algorithm;

import com.tmw.tracking.domain.containercalc.entities.*;
import com.tmw.tracking.domain.containercalc.enums.ContainerPosition;
import com.tmw.tracking.domain.containercalc.enums.PlacementType;
import com.tmw.tracking.entity.ContainerType;

import java.util.ArrayList;
import java.util.List;

public class PipeAlgorithm extends ShapeAlgorithm {

    PipeAlgorithm(Cargo cargo, ContainerType containerType) {
        super(cargo, containerType);
    }

    @Override
    List<PlacementCombination> calculatePlacementCombinations() {

        List<PlacementCombination> placementCombinations = new ArrayList<>();

        final Double K = cargo.getPackageType().getPackingFactor();

        PlacementTypeResult row = createPlacementTypeResult(PlacementType.ROW,
                (int)Math.floor(containerType.getLength() / (cargo.getLength() * K)),
                (int)Math.floor(containerType.getWidth() / (cargo.getDiameter() * K)),
                isLayeringAvailable() ? (int)Math.floor(containerType.getHeight() / (cargo.getDiameter() * K)):
                        (cargo.getDiameter() > containerType.getHeight() ? 0: 1));


        row.setFreeClearance(containerType.getWidth() - row.getWidthQuantity() * cargo.getDiameter() * K);

        PlacementCombination combinationRow = new PlacementCombination();
        combinationRow.getPlacementTypeResults().put(ContainerPosition.A_MAIN, row);


        //FOR LAYER ALIGN
        Integer widthQuantityOdd = (int)Math.floor((containerType.getWidth() / (cargo.getDiameter() * K)) ); //Ubn

        Integer widthQuantityEven = widthQuantityOdd - 1; //Ubc

        Double freeClearance = (containerType.getWidth() - (cargo.getDiameter() * widthQuantityOdd)) / (widthQuantityEven); //g

        Double freeLegde = (containerType.getWidth() - (cargo.getDiameter() * widthQuantityEven + freeClearance * (widthQuantityEven - 1)) ) / 2; //p

        double biasCatheter = (cargo.getDiameter() * K + freeClearance) / 2; //A

        Double nextRowHeight = Math.sqrt( Math.pow(cargo.getDiameter(), 2 ) - Math.pow(biasCatheter, 2)); //Y

        //Integer heightQuantity = (int)Math.floor((containerType.getHeight() / (cargo.getDiameter() * K) )); //Uh
        int heightQuantity = (int)Math.floor( (containerType.getHeight() - (cargo.getDiameter() * K)) / (nextRowHeight * K)) + 1; //Uh
        if (nextRowHeight < (cargo.getDiameter() / 2)) {
            heightQuantity = heightQuantity / 2;
        }

        Integer evenRows = (heightQuantity % 2 == 1) ? (heightQuantity -1) /2 : heightQuantity / 2;
        Integer oddRows = (heightQuantity % 2 == 1) ? evenRows + 1 : heightQuantity / 2;

        int lengthQuantity = (int)Math.floor(containerType.getLength() / (cargo.getLength() * K)); //Ul

        Integer overallQuantity = ((widthQuantityOdd * oddRows) + (widthQuantityEven * evenRows)) * lengthQuantity;


        PlacementTypeResult rowLengthAlign = createPlacementTypeResult(PlacementType.ROW_LAYER_ALIGN,
                lengthQuantity,
                widthQuantityOdd,
                isLayeringAvailable()?heightQuantity: 1);

        rowLengthAlign.setNextRowWidth(nextRowHeight);
        rowLengthAlign.setBiasCatheter(freeLegde);
        rowLengthAlign.setFreeLedge(freeLegde);
        rowLengthAlign.setFreeClearance(freeClearance);

        PlacementCombination combinationLengthAlign = new PlacementCombination();
        combinationLengthAlign.setOverridenOverallQuantity(overallQuantity);
        combinationLengthAlign.getPlacementTypeResults().put(ContainerPosition.A_MAIN, rowLengthAlign);

        placementCombinations.add(combinationRow);
        placementCombinations.add(combinationLengthAlign);

        return placementCombinations;
    }

    @Override
    Double getSpecificLoadingVolume() {
        return cargo.getDiameter() * cargo.getDiameter() * cargo.getLength() / (cargo.getWeight()) / 1000;
    }


    void calculatePlacementPositions(ContainerCalcResult calcResult) {

        PlacementTypeResult placementTypeResult = calcResult.getBestCombination().getPlacementTypeResults().get(ContainerPosition.A_MAIN);
        PlacementType placementType = placementTypeResult.getPlacementType();

        List<CargoPosition> cargoPositions = new ArrayList<>();

        final Dimension distortedCargoDimension = cargo.getDistortedCargoSize(placementType);
        placementTypeResult.setCargoDimension(distortedCargoDimension);

        final Double K = cargo.getPackageType().getPackingFactor();

        Double length = distortedCargoDimension.getLength();
        Double diameter = distortedCargoDimension.getDiameter();
        Double height = distortedCargoDimension.getHeight();


        int offsetx = (int)Math.floor((((placementTypeResult.getLengthQuantity() * length / 10)  ) / 2 - length / 10 / 2 ) * K);
        int offsetz = (int)Math.floor((((placementTypeResult.getWidthQuantity() * diameter / 10) ) / 2 - diameter / 10 / 2 ) * K);
        int offsety = (int)Math.floor(-containerType.getHeight() / 10 / 2 + diameter / 10 / 2);
        if (placementType == PlacementType.ROW_LAYER_ALIGN) {
            offsetz = (int)(Math.floor( calcResult.getContainerType().getWidth() /10/2) - diameter/10/2);
        }

        Integer quantity = 0;
        Integer maxx = 0;
        Integer lastX = 0;

        int finalOffsetX = offsetx;
        int finalOffsetZ = offsetz;
        int finalOffsetY = offsety;


        for (Integer y = 0; y < placementTypeResult.getHeightQuantity(); y += 1) {
            List<CargoPosition> layer = new ArrayList<>();


            if (y > 0) {
                if (placementType == PlacementType.ROW_LAYER_ALIGN) {
                    if (placementTypeResult.getNextRowWidth() > diameter / 2) {
                        offsety += ((y + 1) % 2 == 1) ?
                                (int) Math.floor(diameter / 10 - (diameter / 10 - placementTypeResult.getNextRowWidth() / 10)) :
                                (int) Math.floor(placementTypeResult.getNextRowWidth() / 10);
                    } else {
                        offsety += ((y + 1) % 2 == 1) ?
                                (int) Math.floor(diameter / 10 - placementTypeResult.getNextRowWidth() / 10) :
                                (int) Math.floor(placementTypeResult.getNextRowWidth() / 10);

                    }
                } else {
                    offsety += (int) Math.floor(diameter / 10);
                }
            }

            for (Integer x = 0; x < placementTypeResult.getLengthQuantity(); x += 1) {
                lastX = x;


                for (Integer z = 0; z < placementTypeResult.getWidthQuantity(); z += 1) {

                    if (placementType == PlacementType.ROW_LAYER_ALIGN) {
                        finalOffsetZ = offsetz -
                                (((y +1) % 2 == 1) ?
                                        (z * (int)Math.floor (placementTypeResult.getFreeClearance() / 10)):
                                        ((z * (int)Math.floor(placementTypeResult.getFreeClearance() / 10)))  + (int)Math.floor(placementTypeResult.getFreeLedge() / 10)) ;
                        if ( ((y +1) % 2 == 0) && z == placementTypeResult.getWidthQuantity() -1) {
                            break;
                        }



                    }

                    int posx = (int) Math.floor(x * (length / 10 * K) - finalOffsetX);
                    int posz = (int) Math.floor(z * (diameter / 10 * K) - finalOffsetZ);
                    int posy = offsety;
                    if (quantity < calcResult.getBestCombination().getOverallQuantity()) {
                        CargoPosition cargoPosition = new CargoPosition(posx, posy, posz);
                        cargoPositions.add(cargoPosition);
                        layer.add(cargoPosition);

                        quantity++;
                        maxx = x;

                    }
                }
            }

            if (placementTypeResult.getLengthQuantity() * placementTypeResult.getWidthQuantity() > layer.size() && placementType != PlacementType.ROW_LAYER_ALIGN) {
                Integer centeringOffsetX = (int) Math.floor(((((lastX - maxx) * length / 10)) / 2 - length / 10 / 2) * K);

                for (CargoPosition position : layer) {
                    position.setX(position.getX() + centeringOffsetX);
                }
            }
        }

        placementTypeResult.setCargoPositions(cargoPositions);

        //calcResult.getPlacementTypes().put(placementTypeResult.getPlacementType(), placementTypeResult);
    }

}
