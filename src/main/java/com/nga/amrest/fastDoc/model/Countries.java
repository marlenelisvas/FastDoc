package com.nga.amrest.fastDoc.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.json.JSONObject;

import com.nga.amrest.fastDoc.config.DBConfiguration;

/*
 * AppName: DocGen
 * TableName: DGEN_COUNTRIES
 * 
 * @author	:	Manish Gupta  
 * @email	:	manish.g@ngahr.com
 * @version	:	0.0.1
 */

@Entity
@Table(name = DBConfiguration.COUNTRIES, schema = DBConfiguration.SCHEMA_NAME)
@NamedQueries({ @NamedQuery(name = "Countries.selectAll", query = "SELECT co FROM Countries co") })
public class Countries {

	@Id
	@Column(name = "\"ID\"", columnDefinition = "VARCHAR(32)")
	private String id;

	@Column(name = "\"NAME\"", columnDefinition = "VARCHAR(64)")
	private String name;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String toString() {// overriding the toString() method
		JSONObject obj = new JSONObject();
		obj.put("id", this.getId());
		obj.put("name", this.getName());
		return obj.toString();
	}
}