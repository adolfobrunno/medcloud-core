package com.medcloud.core.persistence.services;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.stereotype.Service;

import com.medcloud.core.entities.Directory;
import com.medcloud.core.entities.Image;
import com.medcloud.core.entities.User;
import com.medcloud.core.exceptions.AuthenticationException;
import com.medcloud.core.persistence.BasicService;
import com.medcloud.core.utils.ImageUtils;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSDBFile;

@Service
public class ImageService extends BasicService<Image> {

	
	@Resource
	private UserService userService;
	@Resource
	GridFsOperations gridOperations;
	@Resource
	DirectoryService directoryService;
	
	public ImageService(){
		super(Image.class);
	}
	
	public void save(Image image, InputStream file, String userToken) throws AuthenticationException{
		
		
		User user = userService.getByToken(userToken);
		if (user == null){
			throw new AuthenticationException("Token inv�lido");
		}else{
			image.setUser_id(user.getId());
			super.save(image);
			DBObject metaData = new BasicDBObject();
			metaData.put("image_properties_id", image.getIid());
			gridOperations.store(file, image.getIid(), metaData);
			createDirectoryTree(image);
		}
	
	}
	
	private void createDirectoryTree(Image image) {
		
		Directory root = new Directory(image.getPid(), null, image.getPid());
		Directory levelOne = new Directory(image.getSno(), root, image.getSno());
		Directory levelTwo = new Directory(image.getSid(), levelOne, image.getSid());
		
		List<Directory> all = new ArrayList<Directory>();
 		all.add(root);
 		all.add(levelOne);
 		all.add(levelTwo);
		directoryService.saveAll(all);
		
	}

	public String downloadImage(Image image, ServletContext servletContext){
		
		String realPath = servletContext.getRealPath("");
	    File dir = new File(realPath+File.separator+"img"+File.separator+image.getCid()+File.separator);
	    if (!dir.exists()){
	       dir.mkdirs();
	    }
		
		GridFSDBFile result = gridOperations.findOne(
				new Query().addCriteria(
						Criteria.where("metadata.image_properties_id").is(image.getIid())));
		String encoded = "";
		if(result != null){
			System.out.println(result.getInputStream());
			try {
				encoded = ImageUtils.getBase64(result.getInputStream());
			} catch (IOException e) {
				encoded = "";
			}
			
		}
		return encoded;
	}
	
	public Image getByTree(String pid, String sno, String sid){
		Query query = new Query(Criteria.where("cid").is(userService.getLoggedUser().getCompany().getCid())
				.and("pid").is(pid).and("sno").is(sno).and("sid").is(sid));
		
		return super.getOne(query);
	}
	
}
