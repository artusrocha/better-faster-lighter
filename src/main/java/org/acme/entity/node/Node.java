package org.acme.entity.node;

import org.acme.entity.attribute.Attribute;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Node implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue
	private Long id;

    private String shortname;

	private String name;

    private String description;
    
    @ManyToMany
    private List<Attribute> attr;
    
	public Node() {}
	
	public Node(String shortname, String name, List<Attribute> attr) {
		this.setShortname(shortname);
		this.setName(name);
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
