package com.tmw.tracking.utils;

import java.util.Properties;

public class PropertyMap {
    Properties properties;

    public PropertyMap(Properties properties) {
        this.properties = properties;
    }

    public String value(String key) {
        return properties.getProperty(key.toLowerCase());
    }
}
