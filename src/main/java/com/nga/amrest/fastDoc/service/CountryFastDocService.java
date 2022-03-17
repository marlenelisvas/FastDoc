package com.nga.amrest.fastDoc.service;

import java.util.List;

import com.nga.amrest.fastDoc.model.ConfigurableColumns;
import com.nga.amrest.fastDoc.model.Countries;

public interface CountryFastDocService {

	public Countries create(Countries item);

	public Countries update(Countries item);

	public void delete(Countries item);

	public List<Countries> findAll();

	public Countries findById(String id);

	public List<Countries> dynamicSelect(List<ConfigurableColumns> requiredColumns);

	List<Countries> findAll(String locale);
}
