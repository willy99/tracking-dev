package com.tmw.tracking.domain.containercalc.algorithm;

import com.tmw.tracking.domain.containercalc.entities.*;
import com.tmw.tracking.domain.containercalc.enums.ContainerPosition;
import com.tmw.tracking.domain.containercalc.enums.PlacementType;
import com.tmw.tracking.entity.ContainerType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BoxAlgorithm extends ShapeAlgorithm {

    BoxAlgorithm(Cargo cargo, ContainerType containerType) {
        super(cargo, containerType);
    }

    @Override
    List<PlacementCombination> calculatePlacementCombinations() {
        List<PlacementCombination> placementTypeCombinations = new ArrayList<>();
        Double K = cargo.getPackageType().getPackingFactor();

        //TODO rewrite to stream. This is shitty piece with 9th nested levels!
        for (PlacementType placementType: PlacementType.values()) {

            //not a side laying - only lateral and longitudinal
            if (!cargo.isSideLaying() && (PlacementType.LATERAL != placementType && PlacementType.LONGITUDINAL != placementType)) {
                continue;
            }
            PlacementTypeResult placementTypeResult = calculatePlacement(
                    placementType, K,
                    new Dimension(containerType.getLength(), containerType.getWidth(), containerType.getHeight()),
                    cargo.getDistortedCargoSize(placementType));
            if (!cargo.isSideLaying()) {
                PlacementCombination placementCombination = new PlacementCombination();
                    placementCombination.getPlacementTypeResults().put(ContainerPosition.A_MAIN, placementTypeResult);
                    placementTypeCombinations.add(placementCombination);
            }

            //calculate combinations for small and big rectangular areas
            if (placementTypeResult != null && cargo.isSideLaying()) {
                final Dimension distortedCargoDimension = cargo.getDistortedCargoSize(placementType);
                double bigSquareWidth = containerType.getWidth() - (placementTypeResult.getWidthQuantity() * distortedCargoDimension.getWidth() * K);
                double smallSquareLength = containerType.getLength() - (placementTypeResult.getLengthQuantity() * distortedCargoDimension.getLength() * K);

                List<PlacementTypeResult> bigBoxResultList = getAvailablePlacementResults(K,
                    containerType.getLength(), bigSquareWidth, containerType.getHeight());
                List<PlacementTypeResult> smallBoxResultList = getAvailablePlacementResults(K,
                                smallSquareLength, containerType.getWidth() - bigSquareWidth, containerType.getHeight());



                //only main area is available
                if (bigBoxResultList.isEmpty() && smallBoxResultList.isEmpty()) {
                    PlacementCombination placementCombination = new PlacementCombination();
                    placementCombination.getPlacementTypeResults().put(ContainerPosition.A_MAIN, placementTypeResult);
                    placementTypeCombinations.add(placementCombination);
                } else {
                    if (bigBoxResultList.isEmpty()) {
                        //only small square is available
                        for (PlacementTypeResult smallBoxResult : smallBoxResultList) {
                            PlacementCombination placementCombination = new PlacementCombination();
                            placementCombination.getPlacementTypeResults().put(ContainerPosition.A_MAIN, placementTypeResult);
                            placementCombination.getPlacementTypeResults().put(ContainerPosition.C_SMALL, smallBoxResult);
                            clearPositions(placementCombination, ContainerPosition.C_SMALL);
                            placementTypeCombinations.add(placementCombination);
                        }
                    } else {
                        //big is available
                        for (PlacementTypeResult bigBoxResult : bigBoxResultList) {
                            if (smallBoxResultList.isEmpty()) {
                                PlacementCombination placementCombination = new PlacementCombination();
                                placementCombination.getPlacementTypeResults().put(ContainerPosition.A_MAIN, placementTypeResult);
                                placementCombination.getPlacementTypeResults().put(ContainerPosition.B_BIG, bigBoxResult);
                                clearPositions(placementCombination, ContainerPosition.B_BIG);
                                placementTypeCombinations.add(placementCombination);
                            } else {
                                //both are available
                                for (PlacementTypeResult smallBoxResult : smallBoxResultList) {
                                    PlacementCombination placementCombination = new PlacementCombination();
                                    placementCombination.getPlacementTypeResults().put(ContainerPosition.A_MAIN, placementTypeResult);
                                    placementCombination.getPlacementTypeResults().put(ContainerPosition.B_BIG, bigBoxResult);
                                    placementCombination.getPlacementTypeResults().put(ContainerPosition.C_SMALL, smallBoxResult);
                                    clearPositions(placementCombination, ContainerPosition.B_BIG);
                                    placementTypeCombinations.add(placementCombination);
                                }
                            }
                        }

                    }

                }

            }
        }

        return placementTypeCombinations;
    }

    private void clearPositions(PlacementCombination placementCombination, ContainerPosition nextPosition) {
        if (placementCombination.getPlacementTypeResults().get(ContainerPosition.A_MAIN).getTotalQuantityForPlacementType() == 0 &&
        placementCombination.getPlacementTypeResults().size() > 1) {
            placementCombination.getPlacementTypeResults().put(
                    ContainerPosition.A_MAIN, placementCombination.getPlacementTypeResults().get(nextPosition));
            placementCombination.getPlacementTypeResults().remove(nextPosition);
        }
    }

    private List<PlacementTypeResult> getAvailablePlacementResults(
            final Double K,
            final Double containerLength, final Double containerWidth, final Double containerHeight) {
        List<PlacementTypeResult> placementTypeResults = new ArrayList<>();
        for (PlacementType placementType : PlacementType.values()) {
            PlacementTypeResult result = calculatePlacement(
                    placementType, K,
                    new Dimension(containerLength, containerWidth, containerHeight),
                    cargo.getDistortedCargoSize(placementType));
            if (availableForPlacement(result)) {
                placementTypeResults.add(result);
            }
        }
        return placementTypeResults;
    }

    private boolean availableForPlacement(PlacementTypeResult placementTypeResult) {
        return (placementTypeResult != null &&
                placementTypeResult.getLengthQuantity() > 0 &&
                placementTypeResult.getWidthQuantity() > 0 &&
                placementTypeResult.getHeightQuantity() > 0
        );
    }

    private PlacementTypeResult calculatePlacement(
            final PlacementType placementType, final Double K,
            final Dimension containerDimension,
            final Dimension cargoDimension) {

        return createPlacementTypeResult(placementType,
                (int) Math.floor(containerDimension.getLength() / (cargoDimension.getLength() * K)),
                (int) Math.floor(containerDimension.getWidth() / (cargoDimension.getWidth() * K)),
                isLayeringAvailable() ? (int) Math.floor(containerDimension.getHeight() / (cargoDimension.getHeight() * K)) :
                        (cargoDimension.getHeight() > containerDimension.getHeight() ? 0 : 1) );

    }

    @Override
    Double getSpecificLoadingVolume() {
        return cargo.getWidth() * cargo.getLength() * cargo.getHeight() / (cargo.getWeight()) / 1000;
    }

    void calculatePlacementPositions(ContainerCalcResult calcResult) {

        Integer quantity = 0;

        for (Map.Entry<ContainerPosition, PlacementTypeResult> ptEntry: calcResult.getBestCombination().getPlacementTypeResults().entrySet()) {
            final double K = cargo.getPackageType().getPackingFactor();
            List<CargoPosition> cargoPositions = new ArrayList<>();
            ContainerPosition containerPosition = ptEntry.getKey();
            PlacementTypeResult placementTypeResult = ptEntry.getValue();

            placementTypeResult.setCargoDimension(cargo.getDistortedCargoSize(placementTypeResult.getPlacementType()));


            final Double length = placementTypeResult.getCargoDimension().getLength();
            final Double width = placementTypeResult.getCargoDimension().getWidth();
            final Double height = placementTypeResult.getCargoDimension().getHeight();


            int offsetx = (int) Math.floor((((placementTypeResult.getLengthQuantity() * length / 10)) / 2 - length / 10 / 2) * K);
            int offsetz = (int) Math.floor((((placementTypeResult.getWidthQuantity() * width / 10)) / 2 - width / 10 / 2) * K);
            int offsety = (int) Math.floor(containerType.getHeight() / 10 / 2 - height / 10 / 2);
            //calculate displacement
            Dimension displacement = calculateDisplacement(calcResult.getBestCombination().getPlacementTypeResults(), containerPosition);
            offsetx += displacement.getLength();
            offsetz += displacement.getWidth();

            //begin place cargo

            int finalOffsetX = offsetx;
            int finalOffsetZ = offsetz;
            int finalOffsetY = offsety;


            for (Integer y = 0; y < placementTypeResult.getHeightQuantity(); y += 1) {
                List<CargoPosition> layer = new ArrayList<>();
                int layerXQuantity = 0;


                for (Integer x = 0; x < placementTypeResult.getLengthQuantity(); x += 1) {
                    if (quantity < calcResult.getBestCombination().getOverallQuantity()) {
                        layerXQuantity++;
                    }


                    for (Integer z = 0; z < placementTypeResult.getWidthQuantity(); z += 1) {


                        Integer posx = (int) Math.floor(x * (length / 10 * K) - finalOffsetX);
                        Integer posz = (int) Math.floor(z * (width / 10 * K) - finalOffsetZ);
                        Integer posy = (int) Math.floor(y * (height / 10 * K) - finalOffsetY);
                        if (quantity < calcResult.getBestCombination().getOverallQuantity()) {
                            CargoPosition cargoPosition = new CargoPosition(posx, posy, posz);
                            cargoPositions.add(cargoPosition);
                            layer.add(cargoPosition);

                            quantity++;
                        }
                    }
                }

                int xDifference = placementTypeResult.getLengthQuantity() - layerXQuantity;
                if (placementTypeResult.getLengthQuantity() * placementTypeResult.getWidthQuantity() > layer.size() && xDifference > 0) {
                    Integer centeringOffsetX = (int) Math.floor(containerType.getLength() / 10 / 2 - (layerXQuantity * length) / 10 /2 -
                            ((containerType.getLength() - placementTypeResult.getLengthQuantity() * length) / 10 / 2 )) ;

                    for (CargoPosition position : layer) {
                        position.setX(position.getX() + centeringOffsetX);
                    }
                }
            }
            placementTypeResult.setCargoPositions(cargoPositions);
        }

    }

    /**
        For each rectangular area the cargo displacement should be in respect to other filled areas.
     */
    private Dimension calculateDisplacement(Map<ContainerPosition, PlacementTypeResult> placementTypeResults, ContainerPosition position) {
        PlacementTypeResult mainPlacement = placementTypeResults.get(ContainerPosition.A_MAIN);
        mainPlacement.setCargoDimension(cargo.getDistortedCargoSize(mainPlacement.getPlacementType()));
        PlacementTypeResult bigPlacement = placementTypeResults.get(ContainerPosition.B_BIG);
        PlacementTypeResult smallPlacement = placementTypeResults.get(ContainerPosition.C_SMALL);

        final double K = cargo.getPackageType().getPackingFactor();
        double bigSquareWidth = containerType.getWidth() - (mainPlacement.getWidthQuantity() * mainPlacement.getCargoDimension().getWidth() * K);
        double smallSquareLength = containerType.getLength() - (mainPlacement.getLengthQuantity() * mainPlacement.getCargoDimension().getLength() * K);
        double x = 0;
        double z = 0;

        if (ContainerPosition.C_SMALL == position) {
            x += (containerType.getLength() - smallSquareLength) / 10 / 2;
            z -= bigPlacement != null ? (bigSquareWidth) / 10 / 2 : 0;
        }
        if (ContainerPosition.A_MAIN == position) {
            x -= smallPlacement != null ? (smallSquareLength / 10 / 2) : 0;
            z-= bigPlacement != null ? (bigSquareWidth) / 10 / 2 :  0;
        }
        if (ContainerPosition.B_BIG == position) {
            z += (containerType.getWidth() - bigSquareWidth) / 10 / 2;
            //x -= smallPlacement != null ? (smallSquareLength / 10 /2) : 0;
        }
        return new Dimension(x, z, 0d);
    }


}
