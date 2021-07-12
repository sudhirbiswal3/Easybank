package com.mycom.demo.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.mycom.demo.model.Role;

@Repository
public interface RoleRepository extends CrudRepository<Role, String> {
	Role findByRole(String role);
}
