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

	public void save(T ct) {
        mongoOperation.save(ct);
    }
 
    public void remove(T ct) {
        mongoOperation.remove(ct);
    }
 
    public T getById(String id) {
        return mongoOperation.findById(id, clazz);
    }
 
    public List<T> getAll() {
        return mongoOperation.findAll(clazz);
    }
    
    public T getOne(Query query){
    	return mongoOperation.findOne(query, clazz);
    }
    
    public List<T> get(Query query){
    	return mongoOperation.find(query, clazz);
    }
	
	public MongoOperations getMongoOperations(){
		return this.mongoOperation;
	}
	
	public void saveAll(List<T> list){
		for (T t : list) {
			this.save(t);
		}
	}
	
}
