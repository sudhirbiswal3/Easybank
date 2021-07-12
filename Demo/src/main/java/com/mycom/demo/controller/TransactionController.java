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

import com.mycom.demo.model.Transaction;
import com.mycom.demo.model.UserEntity;
import com.mycom.demo.services.TransactionService;
import com.mycom.demo.services.UserService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class TransactionController {
	@Autowired
	private TransactionService tranService;
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "/transaction/add", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<?>  addTransaction(@RequestBody Transaction transaction) {
		Transaction tran = null;
		try {		
			tran =  tranService.saveTransactions(transaction);
			if(tran.getId()!=null)
			{
				return ResponseEntity.ok(tran);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error");
		}
		return ResponseEntity.ok(tran);
	}
	
	@RequestMapping(value = "/transaction/list", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<?>  listTransaction(@RequestBody Map<String, String> userInfo) {
		UserEntity foundUser = userService.getUser(userInfo.get("email"));
		List<Transaction> tranList =  tranService.getTransactionsByDate(foundUser.getEmail());
		if(tranList!=null && !tranList.isEmpty())
		{
			return ResponseEntity.ok(tranList);
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error");
	}
	
	@RequestMapping(value = "/transaction/balance", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<?>  getAllTransaction(@RequestBody Map<String, String> userInfo) {
		UserEntity foundUser = userService.getUser(userInfo.get("email"));
		List<Transaction> tranList =  tranService.getAllTransactions(foundUser.getEmail());
		Double totalTransaction = 0d;
		if(tranList!=null && !tranList.isEmpty())
		{
			totalTransaction =  tranList.stream().mapToDouble(s -> {
				return s.getTranAmount();
			}).sum();
			return ResponseEntity.ok(tranList);
		}
		return ResponseEntity.ok(totalTransaction);
	}
}
