package com.tmw.tracking.domain.flex.entities;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tmw.tracking.entity.TenantSpecificEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name="flex_warehouse")
@JsonIgnoreProperties(ignoreUnknown = true)
@Embeddable
public class FlexWarehouse extends TenantSpecificEntity {

    @NotNull(message = "Warehouse Name can't be null")
    @Column(nullable = false, name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(unique = false, nullable = false, name="warehouse_type")
    private FlexWarehouseTypeEnum warehouseType;

    public String getName() {
        return name;
    }

    public FlexWarehouseTypeEnum getWarehouseType() {
        return warehouseType;
    }

    public void setWarehouseType(FlexWarehouseTypeEnum warehouseType) {
        this.warehouseType = warehouseType;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        FlexWarehouse that = (FlexWarehouse) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
