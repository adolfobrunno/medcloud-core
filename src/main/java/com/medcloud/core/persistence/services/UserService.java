package com.medcloud.core.persistence.services;

import java.util.UUID;

import javax.annotation.Resource;

import org.jasypt.exceptions.EncryptionOperationNotPossibleException;
import org.jasypt.spring.security3.PasswordEncoder;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.medcloud.core.entities.User;
import com.medcloud.core.exceptions.AuthenticationException;
import com.medcloud.core.persistence.BasicService;

@Service
public class UserService extends BasicService<User>{

	@Resource
	PasswordEncoder pe;
	
	public UserService(){
		super(User.class);
	}

	public User getByUsername(String username){
		return this.getOne(new Query(Criteria.where("username").is(username)));
	}
	
	public String authenticate(String username, String password) throws AuthenticationException{
		
		try{
			User user = this.getByUsername(username);
			if(user != null && pe.isPasswordValid(user.getPassword(), password, null)){
				return user.getToken();
			}else{
				throw new AuthenticationException("Invalid username / password");
			}
		}catch (EncryptionOperationNotPossibleException ex) {
			throw new AuthenticationException("Invalid username / password");
		}
		
	}
	
	public void save(User user){
		user.setToken(UUID.randomUUID().toString());
		user.setPassword(pe.encodePassword(user.getPassword(), null));
	}
	
	public User getByToken(String token){
		return this.getOne(new Query(Criteria.where("token").is(token)));
	}
	
	public User getLoggedUser(){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return this.getByUsername(auth.getName());
	}
	
	
	
}
