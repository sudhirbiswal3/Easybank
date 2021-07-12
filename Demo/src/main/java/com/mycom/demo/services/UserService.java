package com.mycom.demo.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.mycom.demo.model.Role;
import com.mycom.demo.model.Transaction;
import com.mycom.demo.model.User;
import com.mycom.demo.model.UserEntity;
import com.mycom.demo.repository.UserRepository;
import com.mycom.demo.validator.Validator;

@Service
@Transactional
public class UserService {
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	TransactionService tranService;
	
	public UserService() {
	
	}
	
	public List<UserEntity> getUsers() {
		return (List<UserEntity>) userRepo.findAll();
	}
	
	public UserEntity getUser(String email) {
		return userRepo.findByEmail(email);
	}
	
	// check if names and email are valid format
	public boolean isValid(String firstName, String lastName, String email) {
		boolean isEmailValid = Validator.isValidEmail(email);
		boolean isFirstNameValid = Validator.isValidName(firstName);
		boolean isLastNameValid = Validator.isValidName(lastName);
		return isEmailValid && isFirstNameValid && isLastNameValid;
	}
	
	public boolean addUser(UserEntity newUser) {
		if (newUser == null)
			return false;
		
		if (userRepo.existsByEmail(newUser.getEmail())) {
			return false;
		}
		
		if (!newUser.getPassword().equals(newUser.getConfirmPassword())) {
			return false;
		}
		
		if (isValid(newUser.getFirstName(), newUser.getLastName(), newUser.getEmail())) {
			// if names fields and email are valid format
			String encodedPassword = new BCryptPasswordEncoder().encode(newUser.getPassword()); 
			newUser.setPassword(encodedPassword);
			newUser.setConfirmPassword(encodedPassword);
			newUser.addRole(new Role(newUser.getRoleType(), newUser));
			userRepo.save(newUser);
			return true;
		}
		return false;
	}
	
	public boolean updateUser(String oldUserName, UserEntity updated_user) {
		UserEntity foundUser = userRepo.findByEmail(oldUserName);
		if (foundUser == null)
			return false;
		
		if (oldUserName.equals(updated_user.getEmail())) {
			if (isValid(updated_user.getFirstName(), updated_user.getLastName(), updated_user.getEmail())) {
				foundUser.setFirstName(updated_user.getFirstName());
				foundUser.setLastName(updated_user.getLastName());
				userRepo.save(foundUser);
			} else {
				return false;
			}
		} else {
			if (userRepo.existsByEmail(updated_user.getEmail()))
				return false;
			
			if (isValid(updated_user.getFirstName(), updated_user.getLastName(), updated_user.getEmail()) ) {
				foundUser.setFirstName(updated_user.getFirstName());
				foundUser.setLastName(updated_user.getLastName());
				foundUser.setEmail(updated_user.getEmail());
				userRepo.save(foundUser);
			} else {
				return false;
			}
		}
		return true;		
	}
	
	public boolean deleteUser(String email) {
		UserEntity foundUser = userRepo.findByEmail(email);
		if (foundUser == null)
			return false;
		userRepo.deleteById(email);
		return true;
	}
	
	public void populateUserInitDetails(UserEntity foundUser, User user) {
		try {
			List<Transaction> transList = tranService.getTransactionsByDate(foundUser.getEmail());
			List<Transaction> allTranList = tranService.getAllTransactions(foundUser.getEmail());
			Double totalBalance = 0d;
			if(allTranList!=null && !allTranList.isEmpty())
			{
				totalBalance =  allTranList.stream().mapToDouble(s -> {
					return s.getTranAmount();
				}).sum();			
			}
			BeanUtils.copyProperties(foundUser, user);
			user.setTransList(transList);	
			user.setTotalBalance(totalBalance.toString());
		} catch (Exception e) {
			
		}
	}
}
