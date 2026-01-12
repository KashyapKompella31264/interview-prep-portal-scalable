package com.example.demo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "courses")
public class Course {

    @Id
    private String id;
    private String title;
    private String description;
    
    private List<String> instructors; // List of instructor IDs
    
    private List<String> registeredStudents; // List of student IDs
    private List<String> subTopics; // List of subtopics in the course
    

    public Course() {
        this.instructors = new ArrayList<>();
        this.registeredStudents = new ArrayList<>();
        this.subTopics = new ArrayList<>();
    }

    public Course(String title, String description, List<String> instructors,List<String> registeredStudents,List<String> subTopics) {
        this.title = title;
        this.description = description;
        this.instructors = instructors;
        this.registeredStudents = new ArrayList<>();
        this.subTopics = subTopics != null ? subTopics : new ArrayList<>();
    }

    // Getters and Setters
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getInstructors() {
        return instructors;
    }

    public void setInstructors(List<String> instructors) {
        this.instructors = instructors;
    }

    public List<String> getRegisteredStudents() {
        return registeredStudents;
    }

    public void setRegisteredStudents(List<String> registeredStudents) {
        this.registeredStudents = registeredStudents;
    }

    public List<String> getSubTopics() {
        return subTopics;
    }

    public void setSubTopics(List<String> subTopics) {
        this.subTopics = subTopics;
    }
}
