package com.nga.amrest.fastDoc.service;

import java.util.List;

import com.nga.amrest.fastDoc.model.Groups;

public interface GroupsService {
	public Groups create(Groups item);

	public Groups update(Groups item);

	public void delete(Groups item);

	public List<Groups> findAll();

}
