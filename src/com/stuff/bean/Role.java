package com.stuff.bean;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Role implements Serializable{
	private Long id;
	private String name;
	private Set<String> funcs = new HashSet<String>();
	
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
	public Set<String> getFuncs() {
		return funcs;
	}
	public void setFuncs(Set<String> funcs) {
		this.funcs = funcs;
	}
	
}
