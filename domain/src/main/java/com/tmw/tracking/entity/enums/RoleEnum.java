package com.tmw.tracking.entity.enums;

public enum RoleEnum {

    COMPANY_ADMIN("Company Admin"),
    SYSTEM_ADMIN("System Admin");

    private String name;

    RoleEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
