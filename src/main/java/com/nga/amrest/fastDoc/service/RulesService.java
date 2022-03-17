package com.nga.amrest.fastDoc.service;

import java.util.List;

import com.nga.amrest.fastDoc.model.Rules;

public interface RulesService {
	public Rules create(Rules item);

	public Rules update(Rules item);

	public void delete(Rules item);

	public List<Rules> findByRuleID(String ruleID);

	public List<Rules> findByRuleName(String name);
}
