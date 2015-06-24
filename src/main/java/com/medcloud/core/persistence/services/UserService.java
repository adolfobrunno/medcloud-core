package com.medcloud.core.persistence.services;

import java.util.UUID;

import javax.annotation.Resource;

import org.jasypt.exceptions.EncryptionOperationNotPossibleException;
import org.jasypt.spring.security3.PasswordEncoder;
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.medcloud.core.entities.User;
import com.medcloud.core.exceptions.AuthenticationException;
import com.medcloud.core.persistence.BasicService;

@Service
public class UserService extends BasicService<User>{

	BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();

	@Resource
	PasswordEncoder pe;
	
	public UserService(){
		super(User.class);
	}

	public User getByUsername(String username){
		return this.get(new Query(Criteria.where("username").is(username)));
	}
	
	public String authenticate(String username, String password) throws AuthenticationException{
		
		try{
			User user = this.getByUsername(username);
			if(user != null && pe.isPasswordValid(user.getPassword(), password, null)){
				return user.getToken();
			}else{
				throw new AuthenticationException("Usuário / senha inválido");
			}
		}catch (EncryptionOperationNotPossibleException ex) {
			throw new AuthenticationException("Usuário / senha inválido");
		}
		
	}
	
	public void save(User user){
		user.setToken(UUID.randomUUID().toString());
		user.setPassword(pe.encodePassword(user.getPassword(), null));
		super.save(user);
	}
	
	public User getByToken(String token){
		return this.get(new Query(Criteria.where("token").is(token)));
	}
	
	
	
}
