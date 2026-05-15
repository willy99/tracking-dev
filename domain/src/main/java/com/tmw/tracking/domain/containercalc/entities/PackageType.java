package com.tmw.tracking.domain.containercalc.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tmw.tracking.domain.containercalc.enums.CargoType;
import com.tmw.tracking.domain.containercalc.enums.ShapeType;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PackageType {

    //private Integer id;
    private CargoType cargoType;
    private Double packingFactor;
    private ShapeType shapeType;
    private Boolean defaultSideLaying;
    private Boolean defaultLayering;

    private PackageType() {

    }

    private PackageType(PackageTypeBuilder builder) {
        //this.id = builder.id;
        this.cargoType = builder.cargoType;
        this.packingFactor = builder.packingFactor;
        this.shapeType = builder.shapeType;
        this.defaultSideLaying = builder.defaultSideLaying;
        this.defaultLayering = builder.defaultLayering;
    }

    public static class PackageTypeBuilder {
        //Integer id;
        private CargoType cargoType;
        private Double packingFactor;
        private ShapeType shapeType;
        private Boolean defaultSideLaying;
        private Boolean defaultLayering;

        /*public PackageTypeBuilder setId(Integer id) {
            this.id = id;
            return this;
        }*/

        public PackageTypeBuilder setCargoType(CargoType name) {
            this.cargoType = name;
            return this;
        }

        public PackageTypeBuilder setShapeType(ShapeType shapeType) {
            this.shapeType = shapeType;
            return this;
        }

        public PackageTypeBuilder setPackingFactor(Double packingFactor) {
            this.packingFactor = packingFactor;
            return this;
        }

        public PackageTypeBuilder setDefaultSideLaying(Boolean defaultSideLaying) {
            this.defaultSideLaying = defaultSideLaying;
            return this;
        }

        public PackageTypeBuilder setDefaultLayering(Boolean defaultLayering) {
            this.defaultLayering = defaultLayering;
            return this;
        }

        public PackageType build() {
            if (cargoType == null || packingFactor == null || shapeType == null || defaultSideLaying == null || defaultLayering == null)  {
                throw new RuntimeException("Not all parameters specified");
            }
            return new PackageType(this);
        }
    }

    public CargoType getCargoType() {
        return cargoType;
    }
    /*public Integer getId() {
        return id;
    }*/

    public Double getPackingFactor() {
        return packingFactor;
    }
    public ShapeType getShapeType() { return shapeType;}

    public String getDefaultSideLaying() {
        return defaultSideLaying.toString();
    }

    public String getDefaultLayering() {return defaultLayering.toString();}
}
