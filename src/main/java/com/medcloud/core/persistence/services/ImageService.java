package com.medcloud.core.persistence.services;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.stereotype.Service;

import com.medcloud.core.entities.Image;
import com.medcloud.core.entities.User;
import com.medcloud.core.exceptions.AuthenticationException;
import com.medcloud.core.persistence.BasicService;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSDBFile;

@Service
public class ImageService extends BasicService<Image> {

	
	@Resource
	private UserService userService;
	@Resource
	GridFsOperations gridOperations;
	
	public ImageService(){
		super(Image.class);
	}
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	
	public void save(Image image, InputStream file, String userToken) throws AuthenticationException{
		
		
		logger.info("Starts to save image: " + image.getIid());
		User user = userService.getByToken(userToken);
		if (user == null){
			logger.error("Failed to authenticate user token");
			throw new AuthenticationException("Token inválido");
		}else{
			image.setUser_id(user.getId());
			super.save(image);
			DBObject metaData = new BasicDBObject();
			metaData.put("image_properties_id", image.getIid());
			gridOperations.store(file, image.getIid()+"_"+image.getUser_id(), metaData);
			logger.info("Image successfully saved!");
		}
	
	}
	
	public List<Image> loadByLoggedUser(ServletContext servletContext){

		User user = userService.getLoggedUser();
		List<Image> images = new ArrayList<Image>();
		Query query = new Query(Criteria.where("user_id").is(user.getId()));
		images = super.get(query);
		
		String realPath = servletContext.getRealPath("");
		String relativePath = servletContext.getContextPath()+"/img/"+user.getUsername()+"/";
	    File dir = new File(realPath+File.separator+"img"+File.separator+user.getUsername()+File.separator);
	    if (!dir.exists()){
	       dir.mkdirs();
	    }
	    System.out.println(dir.getAbsolutePath());
		for (Image image : images) {
			GridFSDBFile result = gridOperations.findOne(
					new Query().addCriteria(
							Criteria.where("metadata.image_properties_id").is(image.getIid())));
			if(result != null){
				File f = new File(dir+File.separator+result.getFilename()+".png");
				try {
					if(!f.exists()){
						result.writeTo(f);
					}
					image.setAbsolutePath(relativePath+result.getFilename()+".png");
				} catch (IOException e) {
					logger.error("Error in write image in disk. Returning null.");
					image.setAbsolutePath(null);
				}
			}
		}
		return images;
	}
	
	
}
