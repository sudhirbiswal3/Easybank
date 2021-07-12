package com.mycom.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.mycom.demo.services.MyUserDetailsService;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private JwtRequestFilter jwtRequestFilter;
	
	@Autowired
	private MyUserDetailsService myUserDetailsService;
		
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(myUserDetailsService);
	}	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
	    
		http.csrf().disable().authorizeRequests()
		.antMatchers("/users/all").hasAuthority("ADMIN")
		.antMatchers("/roles/**").hasAuthority("ADMIN")
		.antMatchers("/admin/**").hasAuthority("ADMIN")
		.antMatchers("/users/**").permitAll()
		.antMatchers("/login").permitAll()
		.antMatchers("/register").permitAll()		
		.anyRequest().authenticated()
		.and()
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	
		// excluded the preflight requests from authorization 
		// our API expects an authorization token in the OPTIONS request as well
		// therefore, we need to exclude OPTIONS requests from authorization checks
		http.cors();
		
		http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
	}	


	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder(12);
	}
	
}
