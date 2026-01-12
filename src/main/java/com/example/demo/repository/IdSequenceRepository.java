package com.example.demo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.IdSequence;
@Repository
public interface IdSequenceRepository extends MongoRepository<IdSequence, String> {
}
