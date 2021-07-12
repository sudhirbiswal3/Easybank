package com.mycom.demo.model;

public class UpdateUserInfo {
	private String oldEmail;
	private UserEntity updatedUser;
	
	public UpdateUserInfo(String oldEmail, UserEntity updatedUser) {
		super();
		this.oldEmail = oldEmail;
		this.updatedUser = updatedUser;
	}

	public String getOldEmail() {
		return oldEmail;
	}

	public UserEntity getUpdatedUser() {
		return updatedUser;
	}
	
}
