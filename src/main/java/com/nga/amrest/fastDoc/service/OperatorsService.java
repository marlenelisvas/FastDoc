package com.nga.amrest.fastDoc.service;

import java.util.List;

import com.nga.amrest.fastDoc.model.Operators;

public interface OperatorsService {
	public Operators create(Operators item);

	public Operators update(Operators item);

	public void delete(Operators item);

	public List<Operators> findAll();

}
