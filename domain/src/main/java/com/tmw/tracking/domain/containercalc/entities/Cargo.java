package com.tmw.tracking.domain.containercalc.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tmw.tracking.domain.containercalc.enums.PlacementType;
import com.tmw.tracking.domain.containercalc.enums.ShapeType;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Cargo {
    private String name;
    private PackageType packageType;
    private Dimension dimension;
    private Double weight;
    private Double volume;
    private Boolean sideLaying;
    private Boolean layering;

    private Cargo() {

    }

    private Cargo(CargoBuilder builder) {
        this.packageType = builder.packageType;

        this.dimension = builder.dimension;
        this.name = builder.name;

        this.weight = builder.weight;
        this.sideLaying = builder.sideLaying;
        this.layering = builder.layering;
    }

    public static class CargoBuilder {

        private String name;
        private PackageType packageType;
        private Double weight;
        private Dimension dimension;
        private Boolean sideLaying;
        private Boolean layering;

        public Cargo build() {
            if (packageType == null) {
                throw new RuntimeException("Package type is not specified");
            }
            if (sideLaying == null) {sideLaying = false;}
            if (name == null || dimension == null || layering == null) {
                throw new RuntimeException("Not all parameters specified for Shape " + packageType.getShapeType().name());
            }
            if ((packageType.getShapeType() == ShapeType.BOX || packageType.getShapeType() == ShapeType.PIPE) && dimension.getLength() == null) {
                    throw new RuntimeException("Not all parameters specified for BOX type");
            }
            return new Cargo(this);
        }

        public CargoBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public CargoBuilder setPackageType(PackageType packageType) {
            this.packageType = packageType;
            return this;
        }

        public CargoBuilder setDimension(Dimension dimension) {
            this.dimension = dimension;
            return this;
        }

        public CargoBuilder setWeight(Double weight) {
            this.weight = weight;
            return this;
        }

        public CargoBuilder setSideLaying(Boolean sideLaying) {
            this.sideLaying = sideLaying;
            return this;
        }

        public CargoBuilder setLayering(Boolean layering) {
            this.layering = layering;
            return this;
        }
    }

    public Double getVolume() {
        if (volume == null) {
            if (ShapeType.BOX == packageType.getShapeType()) {
                this.volume = this.dimension.getLength() * this.dimension.getWidth() * this.dimension.getHeight();
            } else
            if (ShapeType.CYLINDER == packageType.getShapeType()) {
                this.volume = this.dimension.getWidth() * this.dimension.getWidth() * this.dimension.getHeight();
            } else if (ShapeType.PIPE == packageType.getShapeType()) {
                this.volume = this.dimension.getWidth() * this.dimension.getWidth() * this.dimension.getLength();
            }
        }
        return volume;
    }

    public String getName() {
        return name;
    }

    public PackageType getPackageType() {
        return packageType;
    }

    public Double getLength() {
        //if (ShapeType.BOX == getPackageType().getShapeType() || ShapeType.CYLINDER == getPackageType().getShapeType()) {
            return dimension.getLength();
        //}
        //else throw new UnsupportedOperationException("Shape type doesn't consider getting length");
    }

    public Double getWidth() {
        //if (ShapeType.BOX == getPackageType().getShapeType()) {
            return dimension.getWidth();
        //}
        //else throw new UnsupportedOperationException("Shape type doesn't consider getting width");
    }

    public Double getDiameter() {
        //if (ShapeType.CYLINDER == getPackageType().getShapeType() || ShapeType.PIPE == getPackageType().getShapeType()) {
            return dimension.getWidth();
        //}
        //else throw new UnsupportedOperationException("Shape type doesn't consider getting diameter");
    }

    public Dimension getDimension() {
        return dimension;
    }

    public void setDimension(Dimension dimension) {
        this.dimension = dimension;
    }

    public Double getHeight() {
        return dimension.getHeight();
    }

    public Double getWeight() {
        return weight;
    }

    public Boolean isSideLaying() {return sideLaying;}

    public Boolean isLayering() {return layering;}

    public Dimension getDistortedCargoSize(PlacementType placementType) {

        switch (placementType) {
            case LATERAL:
                return new Dimension(this.dimension.getWidth(), this.dimension.getLength(), this.dimension.getHeight());
            case LONGITUDINAL:
                return new Dimension(this.dimension.getLength(), this.dimension.getWidth(), this.dimension.getHeight()); //original
            case VERTICAL_LATERAL:
                return new Dimension(this.dimension.getHeight(), this.dimension.getWidth(), this.dimension.getLength());
            case VERTICAL_LONGITUDINAL:
                return new Dimension(this.dimension.getWidth(), this.dimension.getHeight(), this.dimension.getLength());
            case EDGE_LATERAL:
                return new Dimension(this.dimension.getHeight(), this.dimension.getLength(), this.dimension.getWidth());
            case EDGE_LONGITUDINAL:
                return new Dimension(this.dimension.getLength(), this.dimension.getHeight(), this.dimension.getWidth());

            default: return new Dimension(this.dimension.getLength(), this.dimension.getWidth(), this.dimension.getHeight());
        }
    }

}
