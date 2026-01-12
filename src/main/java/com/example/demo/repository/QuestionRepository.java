package com.example.demo.repository;

import com.example.demo.model.Question;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface QuestionRepository extends MongoRepository<Question, String> {

	
}