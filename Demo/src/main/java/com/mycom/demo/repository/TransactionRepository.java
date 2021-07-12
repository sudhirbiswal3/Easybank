package com.mycom.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mycom.demo.model.Transaction;
@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Long> {
	
	@Query(value = "SELECT * FROM Transaction t WHERE t.trans_date >= ( CURRENT_DATE - INTERVAL 7 DAY)  and t.email = :email",nativeQuery = true)
	public List<Transaction> getAllBetweenDates(@Param("email") String email);
	
	@Query(value = "from Transaction t where t.email = :email ")
	public List<Transaction> findAllByEmail(@Param("email") String email);
}
