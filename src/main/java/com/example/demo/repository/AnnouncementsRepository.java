package com.example.demo.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Announcements;

@Repository
public interface AnnouncementsRepository extends MongoRepository<Announcements, String>{
	List<Announcements> findByCourseIdOrderByCreatedAtDesc(String courseId);
}
