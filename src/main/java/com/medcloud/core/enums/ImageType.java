package com.medcloud.core.enums;

public enum ImageType {
	
	CT("Tomografia Computadorizada"),
	NM("Medicina Nuclear"),
	MRI("Ressonância Magnética"),
	US("Ultrassom"),
	DS("Angiografia Digital"),
	DM("Microscopia Eletrônica Digital"),
	DCM("Microscopia Digital a Cores"),
	CLL("Imagens de Luz Colorida"),
	CRDR("Radiografia Digital / Computadorizada"),
	DX("Raio-X Digitalizado"),
	DMM("Mamografia Digital");

	
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
