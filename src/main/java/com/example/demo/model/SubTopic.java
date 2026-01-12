package com.example.demo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document
public class SubTopic {
	@Id
	private String id;
    private String title; // Title of the subtopic
    private List<String> questions=new ArrayList<>(); // List of questions under this subtopic

    public SubTopic() {}

    public SubTopic(String id,String title, List<String> questions) {
    	this.id=id;
        this.title = title;
        this.questions = questions;
    }
    
    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getQuestions() {
        return questions;
    }

    public void setQuestions(List<String> questions) {
        this.questions = questions;
    }
}
