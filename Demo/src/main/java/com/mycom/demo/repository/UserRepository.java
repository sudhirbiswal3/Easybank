

package com.mycom.demo.repository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.mycom.demo.model.UserEntity;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, String> {
	UserEntity findByEmail(String email);
	boolean existsByEmail(String email);
}
