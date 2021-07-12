package com.mycom.demo.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.mycom.demo.model.AuthRequest;
import com.mycom.demo.model.AuthResponse;
import com.mycom.demo.model.Role;
import com.mycom.demo.model.UserEntity;
import com.mycom.demo.repository.UserRepository;
import com.mycom.demo.security.JwtUtil;
import com.mycom.demo.validator.Validator;

@Service
@Transactional
public class AuthService {
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	UserService userService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	public boolean isFilled(String field) {
		return field != null && !field.equals("");
	}
	
	public AuthResponse validateLogin(AuthRequest authReq) {
		AuthResponse authRes = new AuthResponse();
		
		// check if username & password are filled
		boolean userNameFilled = isFilled(authReq.getEmail());
		boolean passwordFilled = isFilled(authReq.getPassword());
		
		// check if username is filled
		if (!userNameFilled) {
			authRes.setEmailErrMsg("Please enter email");
		}
		
		// check if password is filled
		if (!passwordFilled) {
			authRes.setPasswordErrMsg("Please enter password");
		}
		
		// if one of them isn't filled --> return
		if (!userNameFilled || !passwordFilled) {
			authRes.setValid(false);
			return authRes;
		}
		
		// all fields are filled, start authenticate
		try {
			Authentication authObj = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authReq.getEmail(), authReq.getPassword()));
						
			// good authentication, create JWT token
			String jwtToken = jwtUtil.generateToken(authReq.getEmail());
			
			authRes.setJwtToken("Bearer " + jwtToken);
			authRes.setValid(authObj.isAuthenticated());
			authRes.setEmailErrMsg(null);
			authRes.setPasswordErrMsg(null);
			authRes.setCurrent_user(userRepo.findByEmail(authReq.getEmail()));
			authRes.setSuccessMsg("You have logged in successfully!");
			
			return authRes;
			
		} catch (AuthenticationException e) {
			authRes.setValid(false);
			// wrong username
			if (!userRepo.existsByEmail(authReq.getEmail())) {
				authRes.setEmailErrMsg("Invalid username");
			} else {
				// correct username, wrong password
				authRes.setPasswordErrMsg("Invalid password");
			}
			return authRes;
		}
	}
	
	public AuthResponse register(UserEntity newUser) {
		AuthResponse authRes = new AuthResponse();
		
		boolean isFirstNameFilled = isFilled(newUser.getFirstName());
		boolean isLastNameFilled = isFilled(newUser.getLastName());
		boolean isRoleTypeFilled = isFilled(newUser.getRoleType());
		boolean isEmailFilled = isFilled(newUser.getEmail());
		boolean isPasswordFilled = isFilled(newUser.getPassword());
		
		boolean isFirstNameValidFormat = Validator.isValidName(newUser.getFirstName());
		boolean isLastNameValidFormat = Validator.isValidName(newUser.getLastName());
		boolean isRoleTypeIsInValidFormat = Validator.isValidRole(newUser.getRoleType());
		boolean isEmailValidFormat = Validator.isValidEmail(newUser.getEmail());
		
		if (!isFirstNameFilled) {
			authRes.setFirstNameErrMsg("Please enter first name");
		} else if (!isFirstNameValidFormat) {
			authRes.setFirstNameErrMsg("Name should NOT contain numbers and special characters");
		}
		
		if (!isLastNameFilled) {
			authRes.setLastNameErrMsg("Please enter last name");
		} else if (!isLastNameValidFormat) {
			authRes.setLastNameErrMsg("Name should NOT contain numbers and special characters");
		}
		
		if (!isRoleTypeFilled) {
			authRes.setRoleTypeErrMsg("Please enter valid role");
		} else if (!isRoleTypeIsInValidFormat) {
			authRes.setRoleTypeErrMsg("Role should NOT contain numbers and special characters");
		}
		
		if (!isEmailFilled) {
			authRes.setEmailErrMsg("Please enter email");
		} else if (!isEmailValidFormat) {
			authRes.setEmailErrMsg("Invalid email format");
		}
		
		if (!isPasswordFilled) {
			authRes.setPasswordErrMsg("Please password");
		}
		
		if (!isFirstNameFilled || !isLastNameFilled || !isEmailFilled || !isPasswordFilled || !isFirstNameValidFormat || !isLastNameValidFormat || !isEmailValidFormat || !isRoleTypeIsInValidFormat ) {
			authRes.setValid(false);
			return authRes;
		}
		
		if (!newUser.getPassword().equals(newUser.getConfirmPassword())) {
			authRes.setConfirmPasswordErrMsg("Passwords not matched");
			authRes.setValid(false);
			return authRes;
		}
		
		if (userRepo.existsByEmail(newUser.getEmail())) {
			authRes.setValid(false);
			authRes.setGeneralErr("This user already exists");
			return authRes;
		}
		
		String encodedPassword = new BCryptPasswordEncoder().encode(newUser.getPassword()); 
		newUser.setPassword(encodedPassword);
		newUser.setConfirmPassword(encodedPassword);
		// new user always has a default role as CUSTOMER
		newUser.addRole(new Role(newUser.getRoleType(), newUser));
		userRepo.save(newUser);
		authRes.setValid(true);
		authRes.setSuccessMsg("Your account has been created! You can login now");
		
		return authRes;
	}
	
}
