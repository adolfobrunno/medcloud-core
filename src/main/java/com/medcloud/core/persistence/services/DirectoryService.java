package com.medcloud.core.persistence.services;

import org.springframework.stereotype.Service;

import com.medcloud.core.entities.Directory;
import com.medcloud.core.persistence.BasicService;

@Service
public class DirectoryService extends BasicService<Directory> {

	public DirectoryService() {
		super(Directory.class);
	}
	
	public Directory getBySNO(String sno){
		
		return super.getById(sno);
	}
	
}
