package com.medcloud.core.persistence.services;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.stereotype.Service;

import com.medcloud.core.entities.Directory;
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
	@Resource
	DirectoryService directoryService;
	
	private final String RAR_TO_DOWNLOAD_NAME = "images.rar";
	
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

	public ImageDownload zipImages(List<String> iids, ServletContext servletContext){
		
		ImageDownload toDownload = new ImageDownload();
		try {
			String realPath = servletContext.getRealPath("");
			File zipFile = new File(realPath + File.separator+"download"+File.separator+RAR_TO_DOWNLOAD_NAME);
			zipFile.getParentFile().mkdirs();
			ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFile)));
			
			List<Image> images = this.getByIds(iids);
		
			for (Image image : images) {
				GridFSDBFile result = gridOperations.findOne(
						new Query().addCriteria(
								Criteria.where("metadata.image_properties_id").is(image.getIid())));
				
				ZipEntry ze = new ZipEntry(result.getFilename()+".dcm");
				zos.putNextEntry(ze);
				InputStream is = result.getInputStream();
				byte[] bytes = new byte[(int) result.getLength()];
				int count = is.read(bytes);
				while (count > -1){
				    zos.write(bytes, 0, count);
				    count = is.read(bytes);
				}
				is.close();
				zos.closeEntry();
			}
			zos.close();
			toDownload.setFileName(zipFile.getName());
			toDownload.setFilePath(zipFile.getAbsolutePath());
			toDownload.setZipOutputStream(zos);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return toDownload;
	}
	
	public List<Image> getByIds(List<String> list){
		
		List<Image> images = new ArrayList<Image>();
		
		for (String string : list) {
			images.add(this.getById(string));
		}
		
		return images;
	}
	
	public List<Image> getByTree(String pid, String sno, String sid){
		Query query = new Query(Criteria.where("cid").is(userService.getLoggedUser().getCompany().getCid())
				.and("pid").is(pid).and("sno").is(sno).and("sid").is(sid));

		return super.get(query);
	}
	
	public List<Image> filter(ImageFilterDTO dto){

		Criteria criteria = Criteria.where("cid").is(userService.getLoggedUser().getCompany().getCid());
		
		if(dto.creationDateInit != null && !dto.creationDateInit.trim().equals("")){
			criteria.and("creation_date").gte(dto.getCreationDateInit()).lte(dto.getCreationDateEnd());
		} else if(dto.creationDateEnd != null && !dto.creationDateEnd.trim().equals("")){
			criteria.and("creation_date").lte(dto.getCreationDateEnd());
		}
		if(dto.iid != null && !dto.iid.trim().equals("")){
			criteria.and("_id").is(dto.iid);
		}
		if(dto.pid != null && !dto.pid.trim().equals("")){
			criteria.and("pid").is(dto.pid);
		}
		if(dto.sid != null && !dto.sid.trim().equals("")){
			criteria.and("sid").is(dto.sid);
		}
		if(dto.sno != null && !dto.sno.trim().equals("")){
			criteria.and("sno").is(dto.sno);
		}
		if(dto.modality != null && !dto.modality.trim().equals("")){
			criteria.and("modality").is(dto.modality);
		}
		
		return super.get(new Query(criteria));
	}
	
	public static class ImageDownload{
		
		String fileName;
		String filePath;
		ZipOutputStream zipOutputStream;
		
		public void deleteFile(){
			new File(filePath).delete();
		}
		
		public String getFileName() {
			return fileName;
		}
		public void setFileName(String fileName) {
			this.fileName = fileName;
		}
		public ZipOutputStream getZipOutputStream() {
			return zipOutputStream;
		}
		public void setZipOutputStream(ZipOutputStream zipOutputStream) {
			this.zipOutputStream = zipOutputStream;
		}
		public String getFilePath() {
			return filePath;
		}
		public void setFilePath(String filePath) {
			this.filePath = filePath;
		}
		
	}
	
	public static class ImageFilterDTO{
		
		public String pid;
		public String iid;
		public String sid;
		public String sno;
		public String modality;
		public String creationDateInit;
		public String creationDateEnd;
		
		private final DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyyMMdd");
		
		public LocalDate getCreationDateEnd(){
			if(creationDateEnd != null){
				return dtf.parseLocalDate(creationDateEnd);
			}
			return LocalDate.now().plusDays(1);
		}
		
		public LocalDate getCreationDateInit(){
			if(creationDateInit != null){
				return dtf.parseLocalDate(creationDateInit);
			}
			return new LocalDate();
		}
		
	}
	
}
