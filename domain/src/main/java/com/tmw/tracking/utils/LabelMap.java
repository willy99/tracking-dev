package com.tmw.tracking.utils;

import java.util.HashMap;

public class LabelMap {

    private HashMap<String, String> bundle;
    public LabelMap(HashMap<String, String> bundle) {
        this.bundle = bundle;
    }

    public String value(String key) {
        return bundle.get(key.toLowerCase());
    }
}
