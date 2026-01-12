package com.example.demo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.SubTopic;

@Repository
public interface SubTopicRepository extends MongoRepository<SubTopic, String>{
	
}
