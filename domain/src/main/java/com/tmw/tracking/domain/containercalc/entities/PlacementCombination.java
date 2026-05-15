package com.tmw.tracking.domain.containercalc.entities;

import com.tmw.tracking.domain.containercalc.enums.ContainerPosition;
import com.tmw.tracking.domain.containercalc.enums.LoadingType;

import java.util.Map;
import java.util.TreeMap;

public class PlacementCombination {
    private Map<ContainerPosition, PlacementTypeResult> placementTypeResults;


    private Integer maxQuantity;
    private LoadingType loadingType;
    private Integer overridenOverallQuantity; //for pipes
    private ContainerPosition lastPosition = null;



    public PlacementCombination() {
        placementTypeResults = new TreeMap<>();
        loadingType = LoadingType.BY_VOLUME;
    }

    public void maximumWeightExceed(Integer maxQuantity) {
        this.maxQuantity = maxQuantity;
        this.overridenOverallQuantity = maxQuantity;
        this.loadingType = LoadingType.BY_WEIGHT;
        int sum = 0;
        for (Map.Entry<ContainerPosition, PlacementTypeResult> placementEntry: placementTypeResults.entrySet()) {
            PlacementTypeResult placement = placementEntry.getValue();
            sum+=placement.getTotalQuantityForPlacementType();
            if (sum >= maxQuantity) {
                placement.setHeightQuantity( //TODO
                    ( (maxQuantity - sum + placement.getTotalQuantityForPlacementType() ) / (placement.getLengthQuantity() * placement.getWidthQuantity())) + 1);
                lastPosition = placementEntry.getKey();
                break;
            }
        }
        if (sum < this.overridenOverallQuantity) {
            overridenOverallQuantity = sum;
        }

        if (ContainerPosition.A_MAIN == lastPosition) {
            placementTypeResults.remove(ContainerPosition.B_BIG);
            placementTypeResults.remove(ContainerPosition.C_SMALL);
        }
        if (ContainerPosition.B_BIG == lastPosition) {
            placementTypeResults.remove(ContainerPosition.C_SMALL);
        }

    }


    public void setOverridenOverallQuantity(Integer overridenOverallQuantity) {
        this.overridenOverallQuantity = overridenOverallQuantity;
    }

    public Integer getOverallQuantity() {
        if (overridenOverallQuantity != null) {
            return overridenOverallQuantity;
        }
        if (LoadingType.BY_WEIGHT == loadingType) {
            return maxQuantity;
        }

        int sum = 0;
        for (PlacementTypeResult result: placementTypeResults.values()) {
            sum += result.getTotalQuantityForPlacementType();
        }
        return sum;
    }

    public Map<ContainerPosition, PlacementTypeResult> getPlacementTypeResults() {
        return placementTypeResults;
    }

    public Integer getMaxQuantity() {
        return maxQuantity;
    }

    public void setMaxQuantity(Integer maxQuantity) {
        this.maxQuantity = maxQuantity;
    }

    public LoadingType getLoadingType() {
        return loadingType;
    }

    public void setLoadingType(LoadingType loadingType) {
        this.loadingType = loadingType;
    }

    public ContainerPosition getLastPosition() {
        return lastPosition;
    }
}
