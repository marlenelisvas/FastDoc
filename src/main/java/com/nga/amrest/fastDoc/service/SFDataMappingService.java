package com.nga.amrest.fastDoc.service;

import java.util.List;

import com.nga.amrest.fastDoc.model.SFDataMapping;

public interface SFDataMappingService {
	public SFDataMapping create(SFDataMapping item);

	public SFDataMapping update(SFDataMapping item);

	public void delete(SFDataMapping item);

	public List<SFDataMapping> findByKey(String key);

}
