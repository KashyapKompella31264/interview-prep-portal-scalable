package com.example.demo.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "questions") // Marks this class as a MongoDB document
public class Question {

    @Id
    private String id; // MongoDB uses String IDs by default

    private String title;
    private String description;
    private String sampleInput;
    private String sampleOutput;
    private List<TestCase> testcases;
    private String difficulty; // Example values: EASY, MEDIUM, HARD
    private String category; // Example: "Data Structures", "Algorithms", etc.

    public Question() {
    }

    public Question(String title, String description, String difficulty, String category,List<TestCase> testcase) {
        this.title = title;
        this.description = description;
        this.difficulty = difficulty;
        this.category = category;
        this.testcases=testcase;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<TestCase> getTestcases() {
		return testcases;
	}

	public void setTestcases(List<TestCase> testcases) {
		this.testcases = testcases;
	}

	public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
