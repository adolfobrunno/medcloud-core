package com.medcloud.core.enums;

public enum ImageType {
	
	CT("Tomografia Computadorizada");

	
	ImageType(String description){
		this.description = description;
	}
	private String description;

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
