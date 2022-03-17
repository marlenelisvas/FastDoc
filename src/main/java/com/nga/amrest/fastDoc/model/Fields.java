package com.nga.amrest.fastDoc.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.json.JSONObject;

import com.nga.amrest.fastDoc.config.DBConfiguration;

/*
 * AppName: DocGen
 * TableName: DGENC_FIELDS
 * 
 * @author	:	Manish Gupta  
 * @email	:	manish.g@ngahr.com
 * @version	:	0.0.1
 */

@Entity
@Table(name = DBConfiguration.FIELDS, schema = DBConfiguration.SCHEMA_NAME)
@NamedQueries({ @NamedQuery(name = "Fields.selectAll", query = "SELECT F FROM Fields F"),
		@NamedQuery(name = "Fields.findByEntity", query = "SELECT F FROM Fields F WHERE F.entityID = :entityID"),
		@NamedQuery(name = "Fields.findByID", query = "SELECT F FROM Fields F WHERE F.id = :id") })
public class Fields {
	@Id
	@Column(name = "\"ID\"", columnDefinition = "VARCHAR(32)")
	private String id;

	@Column(name = "\"TECHNICAL_NAME\"", columnDefinition = "VARCHAR(64)")
	private String technicalName;

	@Column(name = "\"ENTITY.ID\"", columnDefinition = "VARCHAR(32)")
	private String entityID;

	@Column(name = "\"SELECT_OPTION\"", columnDefinition = "VARCHAR(480)")
	private String selectOption;

	@Column(name = "\"VALUE_FROM_PATH\"", columnDefinition = "VARCHAR(128)")
	private String valueFromPath;

	@Column(name = "\"RULE.ID\"", columnDefinition = "VARCHAR(32)")
	private String ruleID;

	@Column(name = "\"DEFAULT_VALUE\"", columnDefinition = "VARCHAR(32)")
	private String defaultValue;

	@Column(name = "\"DESCRIPTION\"", columnDefinition = "VARCHAR(128)")
	private String description;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "\"ENTITY.ID\"", referencedColumnName = "\"ID\"", insertable = false, updatable = false)
	private Entities entity;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "\"RULE.ID\"", referencedColumnName = "\"ID\"", insertable = false, updatable = false)
	private Rules rule;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTechnicalName() {
		return technicalName;
	}

	public void setTechnicalName(String technicalName) {
		this.technicalName = technicalName;
	}

	public String getEntityID() {
		return entityID;
	}

	public void setEntityID(String entityID) {
		this.entityID = entityID;
	}

	public String getSelectOption() {
		return selectOption;
	}

	public void setSelectOption(String selectOption) {
		this.selectOption = selectOption;
	}

	public String getValueFromPath() {
		return valueFromPath;
	}

	public void setValueFromPath(String valueFromPath) {
		this.valueFromPath = valueFromPath;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Entities getEntity() {
		return entity;
	}

	public void setEntity(Entities entity) {
		this.entity = entity;
	}

	public String getRuleID() {
		return ruleID;
	}

	public void setRuleID(String ruleID) {
		this.ruleID = ruleID;
	}

	public Rules getRule() {
		return rule;
	}

	public void setRule(Rules rule) {
		this.rule = rule;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String toString() {// overriding the toString() method
		JSONObject obj = new JSONObject();
		obj.put("id", this.getId());
		obj.put("technicalName", this.getTechnicalName());
		obj.put("entityID", this.getEntityID());
		obj.put("selectOption", this.getSelectOption());
		obj.put("valueFromPath", this.getValueFromPath());
		obj.put("ruleID", this.getRuleID());
		obj.put("defaultValue", this.getDefaultValue());
		obj.put("description", this.getDescription());
		return obj.toString();
	}
}
