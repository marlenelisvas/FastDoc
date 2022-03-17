package com.nga.amrest.fastDoc.service;

import java.util.List;

import com.nga.amrest.fastDoc.model.ConfigurableTables;

public interface ConfigurableTablesService {
	public ConfigurableTables create(ConfigurableTables item);

	public ConfigurableTables update(ConfigurableTables item);

	public void delete(ConfigurableTables item);

	public List<ConfigurableTables> findAll();

	public ConfigurableTables findById(String id);
}
