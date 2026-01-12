package com.example.demo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "instructors") // Marks this class as a MongoDB document
public class Instructor {

    @Id
    private String id; // MongoDB uses String IDs by default
    private String name;
    private String email;
    private String password;
    private String expertise; // Example: "Data Structures", "System Design", etc.
    private int experience; // Number of years of experience
    public Instructor() {
    	
    }
    public Instructor(String id,String name,String email,String password) {
    	this.id=id;
    	this.name=name;
    	this.email=email;
    	this.password=password;
    }
    
    public Instructor(String name, String email,String password, String expertise, int experience) {
        this.name = name;
        this.email = email;
        this.password=password;
        this.expertise = expertise;
        this.experience = experience;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getExpertise() {
        return expertise;
    }

    public void setExpertise(String expertise) {
        this.expertise = expertise;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }
}
