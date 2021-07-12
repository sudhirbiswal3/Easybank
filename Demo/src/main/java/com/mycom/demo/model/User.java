package com.mycom.demo.model;

import java.util.List;
import java.util.Set;

public class User {

	private String firstName;
	
	private String lastName;
	
	private String roleType;
	
	private String email;
	
	private String password;
	
	private String confirmPassword;
	
	private List<Transaction> transList;
	
	private Set<Role> roles;
	
	private String totalBalance;
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getRoleType() {
		return roleType;
	}
	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getConfirmPassword() {
		return confirmPassword;
	}
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	public List<Transaction> getTransList() {
		return transList;
	}
	public void setTransList(List<Transaction> transList) {
		this.transList = transList;
	}
	public Set<Role> getRoles() {
		return roles;
	}
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	public String getTotalBalance() {
		return totalBalance;
	}
	public void setTotalBalance(String totalBalance) {
		this.totalBalance = totalBalance;
	}	
}
