package com.medcloud.core.persistence.services;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.medcloud.core.entities.Image;
import com.medcloud.core.entities.User;
import com.medcloud.core.exceptions.AuthenticationException;
import com.medcloud.core.persistence.BasicService;

@Service
public class ImageService extends BasicService<Image>{

	
	@Resource
	private UserService userService;
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	
	public void save(Image image, String userToken) throws AuthenticationException{
		
		logger.info("Starts to save image: " + image.getIid());
		User user = userService.getByToken(userToken);
		if (user == null){
			logger.error("Failed to authenticate user token");
			throw new AuthenticationException("Token inválido");
		}else{
			image.setUser(user);
			super.save(image);
			logger.info("Image successfully saved!");
		}
	
	}
	
}
