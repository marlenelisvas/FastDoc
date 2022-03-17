package com.nga.amrest.fastDoc.service;

import java.util.List;

import com.nga.amrest.fastDoc.model.Companies;

public interface CompaniesService {
	public Companies create(Companies item);

	public Companies update(Companies item);

	public void delete(Companies item);

	public List<Companies> findAll();
}
