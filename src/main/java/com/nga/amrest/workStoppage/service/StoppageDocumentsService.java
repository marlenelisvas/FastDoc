package com.nga.amrest.workStoppage.service;

import java.util.List;

import com.nga.amrest.workStoppage.model.StoppageDocuments;

public interface StoppageDocumentsService {
	public List<StoppageDocuments> findAll();

	public StoppageDocuments update(StoppageDocuments item);

	public StoppageDocuments create(StoppageDocuments item);

	public List<StoppageDocuments> findByStoppageDetailsId(String id);
}
