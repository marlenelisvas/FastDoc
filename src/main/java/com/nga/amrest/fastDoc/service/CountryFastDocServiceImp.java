package com.nga.amrest.fastDoc.service;

import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.nga.amrest.fastDoc.model.ConfigurableColumns;
import com.nga.amrest.fastDoc.model.Countries;

@Transactional
@Component
public class CountryFastDocServiceImp implements CountryFastDocService {

	@PersistenceContext
	EntityManager em;

	@Override
	@Transactional
	public Countries create(Countries item) {
		em.persist(item);
		return item;
	}

	@Override
	@Transactional
	public Countries update(Countries item) {
		em.merge(item);
		return item;
	}

	@Override
	@Transactional
	public void delete(Countries item) {
		em.remove(item);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Countries> findAll() {
		Query query = em.createNamedQuery("Countries.selectAll");
		List<Countries> items = query.getResultList();
		return items;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Countries> findAll(String locale) {
		Query query = em.createNamedQuery("Countries.selectAllbasedOnLocale").setParameter("locale", locale);
		List<Countries> items = query.getResultList();
		return items;
	}

	@Override
	public Countries findById(String id) {
		Countries item = em.find(Countries.class, id);
		return item;
	}

	public List<Countries> dynamicSelect(List<ConfigurableColumns> requiredColumns) {
		Iterator<ConfigurableColumns> iterator = requiredColumns.iterator();
		String columnsToSelect = "";
		while (iterator.hasNext()) {
			columnsToSelect = columnsToSelect + "C." + iterator.next().getColumnName() + ",";
		}
		columnsToSelect = columnsToSelect.substring(0, columnsToSelect.length() - 1);
		String query = "SELECT " + columnsToSelect + " FROM Countries C";
		return em.createQuery(query, Countries.class).getResultList();
	}
}