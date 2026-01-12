package com.example.demo.repository;

import com.example.demo.model.Instructor;
import com.example.demo.model.User;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface InstructorRepository extends MongoRepository<Instructor, String> {

	Optional<Instructor> findByEmail(String email);

	Optional<User> findByName(String name);

	List<Instructor> findByIdIn(List<String> instructorIds);
}
