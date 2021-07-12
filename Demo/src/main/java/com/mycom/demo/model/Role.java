package com.mycom.demo.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name="role")
public class Role {
	
	@Id
	@Column(name = "role")
	private String role;
	
	@ManyToMany(mappedBy = "roles", fetch = FetchType.EAGER)
	private Set<UserEntity> users = new HashSet<>();
	
	public Role() {}
	
	public Role(String role, UserEntity user) {
		this.role = role;
		this.users.add(user);
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public void setUsers(Set<UserEntity> users) {
		this.users = users;
	}
	
	
}
