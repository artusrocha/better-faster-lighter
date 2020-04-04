package org.acme.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

@Entity
public class Attribute extends PanacheEntityBase implements Serializable {
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@OneToOne
	private Category key;
	
	@Id
	private String value;

	public Attribute() {}

	public Attribute(String category, String value) {
		this.setKey( new Category(category) );
		this.setValue(value);
	}

	public Attribute(Category category, String value) {
		this.setKey( category );
		this.setValue(value);
	}

	public Category getKey() {
		return key;
	}
	public void setKey(Category key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

	
}
