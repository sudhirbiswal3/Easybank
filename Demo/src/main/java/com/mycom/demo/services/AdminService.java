package com.mycom.demo.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.mycom.demo.model.AdminAddUserRequest;
import com.mycom.demo.model.Role;
import com.mycom.demo.model.UserEntity;
import com.mycom.demo.repository.UserRepository;

@Service
@Transactional
public class AdminService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserService userService;
	
	public UserEntity adminAddUser(AdminAddUserRequest user) {
		boolean isFirstNameFilled = !user.getFirstName().equals("");
		boolean isLastNameFilled = !user.getLastName().equals("");
		boolean isRoleTypeFilled = !user.getRole().equals("");
		boolean isEmailFilled = !user.getEmail().equals("");
		boolean isRoleSelected = !user.getRole().equals("");
				
		if (!isFirstNameFilled || !isLastNameFilled || !isRoleTypeFilled || !isEmailFilled || !isRoleSelected
				|| !userService.isValid(user.getFirstName(), user.getLastName(), user.getEmail())) {
			return null;
		}
		
		String encodedPass = generateBcryptPassword();
		UserEntity newUser = new UserEntity(user.getFirstName(), user.getLastName(),user.getRole(), user.getEmail(), encodedPass, encodedPass);
		newUser.addRole(new Role(user.getRole(), newUser));
		userRepository.save(newUser);
		return newUser;
	}
	
	public String generateBcryptPassword() {
		String encodedPass = new BCryptPasswordEncoder().encode("pass");
		return encodedPass;
	}
}
