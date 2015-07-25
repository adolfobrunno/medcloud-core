package com.medcloud.core.persistence.services;

import org.springframework.stereotype.Service;

import com.medcloud.core.entities.Company;
import com.medcloud.core.persistence.BasicService;

@Service
public class CompanyService extends BasicService<Company>{

	public CompanyService() {
		super(Company.class);
	}
	
}
