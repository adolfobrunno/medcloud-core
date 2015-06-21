package com.medcloud.core.persistence;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;

public class BasicService<T> {

	@Resource
	private MongoOperations mongoOperation;
	
	final Class<T> clazz;

	public BasicService(Class<T> clazz) {
		this.clazz = clazz;
	}
	
	public BasicService() {
		clazz = null;
	}

	@SuppressWarnings("hiding")
	public <T> void save(T ct) {
        mongoOperation.save(ct);
    }
 
    public <T> void remove(T ct) {
        mongoOperation.remove(ct);
    }
 
    public T getBydId(String id) {
        return mongoOperation.findById(id, clazz);
    }
 
    public List getAll() {
        return mongoOperation.findAll(clazz);
    }
    
    public T get(Query query){
    	return mongoOperation.findOne(query, clazz);
    }
	
	
	
}
