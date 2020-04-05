package org.acme.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

@Entity
public class Category extends PanacheEntityBase implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	private String name;

	@OneToMany
	private List<Attribute> values;

	public Category() {}
	
	public Category(String name) {
		this.setName(name);
	}

	public String getName() {
		return name;
	}

	private void setName(String name) {
		this.name = name;
	}

	public List<Attribute> getValues() {
		return values;
	}
}
