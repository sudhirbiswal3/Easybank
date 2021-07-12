package com.mycom.demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mycom.demo.model.UpdateUserInfo;
import com.mycom.demo.model.User;
import com.mycom.demo.model.UserEntity;
import com.mycom.demo.services.UserService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "/users/all", method = RequestMethod.GET, produces = "application/json")
	public List<UserEntity> getUsers() {
		return userService.getUsers();
	}
	
	@RequestMapping(value = "/users/get", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<?> getUser(@RequestBody Map<String, String> userInfo) {
		UserEntity foundUser = userService.getUser(userInfo.get("email"));		
		if (foundUser == null) {			
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(foundUser);
		}
		User user = new User();
		userService.populateUserInitDetails(foundUser, user);
		return ResponseEntity.ok(user);
	}

	
	
	@RequestMapping(value = "/users/add", method = RequestMethod.POST, produces = "application/json")
	public boolean addUser(@RequestBody UserEntity newUser) {
		return userService.addUser(newUser);
	}
	
	@RequestMapping(value = "/users/update", method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<?> updateUser(@RequestBody UpdateUserInfo udpated_userInfo) {
		boolean valid = userService.updateUser(udpated_userInfo.getOldEmail(), udpated_userInfo.getUpdatedUser());
		if (valid) {
			return ResponseEntity.ok(valid);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(valid);
	}
	
	@RequestMapping(value = "/users/delete", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<?> deleteUser(@RequestBody Map<String, String> userInfo) {
		boolean valid = userService.deleteUser(userInfo.get("email"));
		if (valid) {
			return ResponseEntity.ok(valid);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(valid);
	}
}
