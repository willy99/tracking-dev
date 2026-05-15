package com.tmw.tracking.entity.enums;

import java.util.Locale;

public enum Langs {
    en("en", "US"),
    ru("ru", "RU"),
    ua("ua", "UA");

    private String country;
    private String region;

    Langs(String country, String region) {
        this.country = country;
        this.region = region;
    }

    public static Langs getLangs(String shortStr) {
        for (Langs locale_map : Langs.values()) {
            if (locale_map.name().equals(shortStr)) {
                return locale_map;
            }
        }
        return en; //default
    }

    public Locale getLocale() {
        return new Locale.Builder().setLanguage(this.country()).setRegion(this.region()).build();
    }

    public String country() {
        return this.country;
    }

    public String region() {
        return region;
    }

}
