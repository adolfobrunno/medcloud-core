package com.medcloud.core.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.google.gson.Gson;

@Document(collection = "directories")
public class Directory {

	@Id
	private String id;
	private String path;
	private Directory parent;

	
	public Directory(String id, Directory parent, String path) {
		this.id = id;
		this.parent = parent;
		this.path = path;
	}
	
	public Directory(){
		
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public Directory getParent() {
		return parent;
	}
	public void setParent(Directory parent) {
		this.parent = parent;
	}
	
	@Override
	public boolean equals(Object other){
		
		if((other instanceof Directory) && this.getParent().equals(((Directory) other).getParent())
				&& this.path.equals(((Directory) other).getPath())){
			return true;
		}
		return false;
	}
	
	public String toJson(){
		Gson gson = new Gson();
		DirectoryJson json = new DirectoryJson(this);
		
		return gson.toJson(json, DirectoryJson.class);
		
	}
	
	public DirectoryJson toDirectoryJson(){
		return new DirectoryJson(this);
	}
	
	public class DirectoryJson {
		
		public String id;
		public String text;
		public String parent;
		
		public DirectoryJson(Directory d){
			this.id = d.getId();
			this.text = d.getPath();
			this.parent = d.getParent() == null ? "#" : d.getParent().getId();
		}
		
	}
	
}
