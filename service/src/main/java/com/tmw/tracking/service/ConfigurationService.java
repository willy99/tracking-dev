package com.tmw.tracking.service;

import com.tmw.tracking.entity.ConfigurationEntity;

import java.util.Collection;

public interface ConfigurationService {

    Integer getIntegerConfigurationByName(String name, Integer defaultValue);

    Boolean getBooleanConfigurationByName(String name, Boolean defaultValue);

    String getStringConfigurationByName(String name, String defaultValue);

    Collection<ConfigurationEntity> getAllTenantConfigurations();

}
