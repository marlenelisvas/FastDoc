package com.nga.amrest.fastDoc.service;

import java.util.List;

import com.nga.amrest.fastDoc.model.Codelist;

public interface CodelistService {
	public Codelist create(Codelist item);

	public Codelist update(Codelist item);

	public void delete(Codelist item);

	public List<Codelist> findByFieldAndKey(String fieldID, String sfKey);
}
