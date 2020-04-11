package org.acme.entity.category;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

@Entity
public class Category extends PanacheEntityBase implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;

	private String name;

	//@OneToMany
	//private List<Attribute> values;

	public Category() {}

	public Category(String name) {
		this.setName(name);
	}

	public Category(Long id, String name) {
		this.setId(id);
		this.setName(name);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


}
