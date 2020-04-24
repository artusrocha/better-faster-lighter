package org.acme.entity.attribute;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.*;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import org.acme.entity.category.Category;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class Attribute extends PanacheEntityBase implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

//	//@Column(unique = true)
//	@Id
//	@GeneratedValue(generator = "UUID")
//	@GenericGenerator( name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
//	@Column(name = "id", updatable = false, nullable = false)
//	private UUID id ;//= UUID.randomUUID();

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	//@Column(name="key_name")
	//private String keyName;


	@ManyToOne
	private Category key;

	private String value;

	public Attribute() {}

	public Attribute(Category key, String value) {
		this.setKey(key);
		this.setValue(value);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Category getKey() {
		return key;
	}

	public void setKey(Category key) {
		this.key = key;
	}
}
