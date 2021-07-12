package com.mycom.demo.services;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.mycom.demo.model.Transaction;
import com.mycom.demo.repository.TransactionRepository;

@Service
@Transactional
public class TransactionService {
	
	@Autowired
	private TransactionRepository tranRepo;
	
	public TransactionService() {
	
	}
	public List<Transaction> getTransactionsByDate(String email) {
		return tranRepo.getAllBetweenDates(email);
	}	
	public List<Transaction> getAllTransactions(String email) {
		return (List<Transaction>) tranRepo.findAllByEmail(email);
	}
	public Transaction saveTransactions(Transaction transaction) {
		String tranType = transaction.getTranType();
		if(tranType!=null && tranType.trim().equalsIgnoreCase("DEBIT"))
		{
			Double ptranAmount = transaction.getTranAmount();
			transaction.setTranAmount(-ptranAmount);
		}
		transaction.setTransDate(getCurrentDate());
		transaction.setEmail(getUserName());
		return tranRepo.save(transaction);
	}
	
	public String getUserName()
	{
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
				. getPrincipal();
		return userDetails.getUsername();
	}
	private Date getCurrentDate() {
		LocalDate localDate = LocalDate.now();
		Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
		return date;
	}
}
