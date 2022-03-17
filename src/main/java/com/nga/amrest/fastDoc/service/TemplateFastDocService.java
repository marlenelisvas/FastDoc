package com.nga.amrest.fastDoc.service;

import java.util.List;

import com.nga.amrest.fastDoc.model.Templates;

public interface TemplateFastDocService {
	public Templates create(Templates item);

	public Templates update(Templates item);

	public void delete(Templates item);

	public List<Templates> findById(String id);
}
