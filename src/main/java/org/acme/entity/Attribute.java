package org.acme.entity;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.*;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

@Entity
public class Attribute extends PanacheEntityBase implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(unique = true)
	private UUID guid = UUID.randomUUID();

	@Id
	@Column(name="key_name")
	private String keyName;
/*
@Id
@ManyToOne
private Category key;
 */
	
	@Id
	private String value;

	public Attribute() {}

	public Attribute(String keyName, String value) {
		this.setKeyName( keyName );
		this.setValue(value);
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getKeyName() {
		return this.keyName;
	}

	public UUID getGuid() {
		return guid;
	}

	public void setGuid(UUID guid) {
		this.guid = guid;
	}
}
