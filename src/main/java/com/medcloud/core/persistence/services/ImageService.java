package com.medcloud.core.persistence.services;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.medcloud.core.entities.Image;
import com.medcloud.core.entities.User;
import com.medcloud.core.exceptions.AuthenticationException;
import com.medcloud.core.persistence.BasicService;

@Service
public class ImageService extends BasicService<Image> {

	
	@Resource
	private UserService userService;
	
	public ImageService(){
		super(Image.class);
	}
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	
	public void save(Image image, String userToken) throws AuthenticationException{
		
		logger.info("Starts to save image: " + image.getIid());
		User user = userService.getByToken(userToken);
		if (user == null){
			logger.error("Failed to authenticate user token");
			throw new AuthenticationException("Token inválido");
		}else{
			image.setUser_id(user.getId());
			super.save(image);
			logger.info("Image successfully saved!");
		}
	
	}
	
	public List<Image> loadByLoggedUser(){
		User user = userService.getLoggedUser();
		List<Image> images = new ArrayList<Image>();
		Query query = new Query(Criteria.where("user_id").is(user.getId()));
//		BasicQuery query1 = new BasicQuery();
		images = super.get(query);
		return images;
	}
	
}
