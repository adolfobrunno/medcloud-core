package com.medcloud.core.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.medcloud.core.enums.ImageType;

@Document(collection = "images")
public class Image {
	
	@Id
	private String iid;
	private ImageType type;
	private String creation_date;
	private String size;
	private String cid;
	private String user_id;
	@Transient
	private String absolutPath;

	public String getIid() {
		return iid;
	}

	public void setIid(String iid) {
		this.iid = iid;
	}

	public ImageType getType() {
		return type;
	}

	public void setType(ImageType type) {
		this.type = type;
	}

	public String getCreation_date() {
		return creation_date;
	}

	public void setCreation_date(String creation_date) {
		this.creation_date = creation_date;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getAbsolutPath() {
		return absolutPath;
	}

	public void setAbsolutePath(String path) {
		this.absolutPath = path;
	}

}
