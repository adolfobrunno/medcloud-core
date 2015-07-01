package com.medcloud.core.enums;

public enum ImageType {
	
	CT("Tomografia Computadorizada"),
	NM("Medicina Nuclear"),
	MRI("Resson�ncia Magn�tica"),
	US("Ultrassom"),
	DS("Angiografia Digital"),
	DM("Microscopia Eletr�nica Digital"),
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
