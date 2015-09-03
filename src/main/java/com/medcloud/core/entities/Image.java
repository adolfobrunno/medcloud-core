package com.medcloud.core.entities;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.medcloud.core.enums.ImageType;

@Document(collection = "images")
public class Image {
	
	
	@Id
	private String iid; // InstanceID
	private String sid; // StudyID
	private String sno; // Series Number
	private String pid; // PatientID
	private String cid; // CompanyID
	private ImageType modality;
	private LocalDate creation_date;
	private String size;
	private String user_id;

	public String getIid() {
		return iid;
	}

	public void setIid(String iid) {
		this.iid = iid;
	}

	public ImageType getModality() {
		return modality;
	}

	public void setModality(ImageType type) {
		this.modality = type;
	}

	public LocalDate getCreation_date() {
		return this.creation_date;
	}
	
	public void setCreation_date(LocalDate creation_date) {
		this.creation_date = creation_date;
	}
	
	public void setCreation_dateString(String str) {
		DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyyMMdd");
		this.setCreation_date(dtf.parseLocalDate(str));
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

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getSno() {
		return sno;
	}

	public void setSno(String sno) {
		this.sno = sno;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}
	
//	public String createTree(){
//		return TreeUtils.getStringTreeByImage(this);
//	}

}
