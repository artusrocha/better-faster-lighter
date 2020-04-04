package org.acme.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

@Entity
public class Actor extends PanacheEntityBase implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
    private String shortname;
    private String description;
    
    @ManyToMany
    private List<Attribute> attr;
    
	public Actor() {}
	
	public Actor(String shortname, String description, List<Attribute> attr) {
		this.setShortname(shortname);
		this.setDescription(description);
		this.setAttr(attr);
	}
	public String getShortname() {
		return shortname;
	}
	public void setShortname(String shortname) {
		this.shortname = shortname;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<Attribute> getAttr() {
		return attr;
	}
	public void setAttr(List<Attribute> attr) {
		this.attr = attr;
	}
}
