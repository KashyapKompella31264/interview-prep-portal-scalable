package com.example.demo.repository;

import com.example.demo.model.Course;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CourseRepository extends MongoRepository<Course, String> {
	
	List<Course> findCoursesByRegisteredStudentsContaining(String studentId);
	List<Course> findCoursesByInstructorsContaining(String instructorId);
}