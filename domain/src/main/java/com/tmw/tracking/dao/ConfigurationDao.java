package com.tmw.tracking.dao;

import com.tmw.tracking.entity.ConfigurationEntity;

import java.util.List;

public interface ConfigurationDao {

    List<ConfigurationEntity> getAllTenantConfiguraton();
}
