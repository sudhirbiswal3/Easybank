package com.mycom.demo.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mycom.demo.model.Role;
import com.mycom.demo.model.UserEntity;
import com.mycom.demo.repository.UserRepository;

@Service
@Transactional
public class MyUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		System.out.println("In MyUserDetailsService: " + userName);
		
		// this is just auto authenticate credentials (password), not principal (userName)
		
		UserEntity foundUser = userRepository.findByEmail(userName);
		
		if (foundUser == null) {
			System.out.println("User " + userName + " not found!");
			throw new UsernameNotFoundException("User " + userName + " not found!");
		}
		
		return org.springframework.security.core.userdetails.User//
				.withUsername(foundUser.getEmail())
				.password(foundUser.getPassword())
				.authorities(getAuthorities(foundUser.getRoles()))
				.build();
				
	}
	
	
	private Collection<? extends GrantedAuthority> getAuthorities(Set<Role> roles) {
		List<GrantedAuthority> authorities = new ArrayList<>();
		for (Role role:roles) {
			authorities.add(new SimpleGrantedAuthority(role.getRole()));
		}
		return authorities;
    }

}
