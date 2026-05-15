package com.tmw.tracking.domain.containercalc.algorithm;

import com.tmw.tracking.domain.containercalc.entities.Cargo;
import com.tmw.tracking.entity.ContainerType;
import com.tmw.tracking.domain.containercalc.enums.ShapeType;


public class ShapeAlgorithmFactory {

    private ShapeAlgorithmFactory() {

    }

    public static ShapeAlgorithm createAlgorithm(
            Cargo cargo, ContainerType containerType) {
        if (ShapeType.BOX == cargo.getPackageType().getShapeType()) {
            return new BoxAlgorithm(cargo, containerType);
        } else if (ShapeType.CYLINDER == cargo.getPackageType().getShapeType()) {
            return new CylinderAlgorithm(cargo, containerType);
        } else if (ShapeType.PIPE == cargo.getPackageType().getShapeType()) {
            return new PipeAlgorithm(cargo, containerType);
        } else
            throw new RuntimeException("No implemenation for shape " + cargo.getPackageType().getShapeType());
    }
}
