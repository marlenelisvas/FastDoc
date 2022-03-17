package com.nga.amrest.fastDoc.service;

import java.util.List;

import com.nga.amrest.fastDoc.model.FormatSeparators;

public interface FormatSeparatorsService {

	public FormatSeparators create(FormatSeparators item);

	public FormatSeparators update(FormatSeparators item);

	public void delete(FormatSeparators item);

	public List<FormatSeparators> findAll();

	public List<FormatSeparators> findByRuleFieldIdCountry(String mapRuleFieldId, String country);
}
