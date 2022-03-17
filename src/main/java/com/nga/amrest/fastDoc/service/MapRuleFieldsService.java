package com.nga.amrest.fastDoc.service;

import java.util.List;

import com.nga.amrest.fastDoc.model.MapRuleFields;

public interface MapRuleFieldsService {
	public MapRuleFields create(MapRuleFields item);

	public MapRuleFields update(MapRuleFields item);

	public void delete(MapRuleFields item);

	public List<MapRuleFields> findByRuleID(String ruleID);
}
