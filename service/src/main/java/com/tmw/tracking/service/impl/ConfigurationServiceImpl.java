package com.tmw.tracking.service.impl;

import com.tmw.tracking.dao.ConfigurationDao;
import com.tmw.tracking.entity.ConfigurationEntity;
import com.tmw.tracking.service.ConfigurationService;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigurationServiceImpl implements ConfigurationService {

    private final ConfigurationDao configurationDao;

    private static Map<String, ConfigurationEntity> configurationMap = new HashMap<String, ConfigurationEntity>();

    @Inject
    public ConfigurationServiceImpl(
            final ConfigurationDao configurationDao
            ) {
        this.configurationDao = configurationDao;
    }

    private void initializeConfigurationMap() {
        List<ConfigurationEntity> configurationEntityList = configurationDao.getAllTenantConfiguraton();
        for (ConfigurationEntity configuration: configurationEntityList) {
            configurationMap.put(configuration.getName(), configuration);
        }
    }

    @Override
    public Integer getIntegerConfigurationByName(String name, Integer defaultValue) {
        if (configurationMap.isEmpty()) {
            initializeConfigurationMap();
        }
        ConfigurationEntity entity = configurationMap.get(name);
        if (entity == null) {
            return defaultValue;
        }
        return Integer.valueOf(entity.getValue());
    }

    @Override
    public Boolean getBooleanConfigurationByName(String name, Boolean defaultValue) {
        if (configurationMap.isEmpty()) {
            initializeConfigurationMap();
        }
        ConfigurationEntity entity = configurationMap.get(name);
        if (entity == null) {
            return defaultValue;
        }
        return Boolean.valueOf(entity.getValue());

    }

    @Override
    public String getStringConfigurationByName(String name, String defaultValue) {
        if (configurationMap.isEmpty()) {
            initializeConfigurationMap();
        }
        ConfigurationEntity entity = configurationMap.get(name);
        if (entity == null) {
            return defaultValue;
        }
        return entity.getValue();
    }

    @Override
    public Collection<ConfigurationEntity> getAllTenantConfigurations() {
        if (configurationMap.isEmpty()) {
            initializeConfigurationMap();
        }
        return configurationMap.values();
    }
}
