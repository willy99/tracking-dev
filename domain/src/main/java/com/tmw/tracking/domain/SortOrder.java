package com.tmw.tracking.domain;

import com.google.common.base.Strings;

import java.util.Locale;

public enum SortOrder {
    ASC("ASC"), DESC("DESC");

    private String order;

    public static SortOrder of(String value, SortOrder defaultValue) {
        if (Strings.isNullOrEmpty(value)) {
            return defaultValue;
        }

        try {
            value = value.toUpperCase(Locale.US).trim();
            return valueOf(value);
        } catch (Exception ignore) {
            return defaultValue;
        }
    }

    SortOrder(String order) {
        this.order = order;
    }

    public String getSortOrder() {
        return order;
    }
}