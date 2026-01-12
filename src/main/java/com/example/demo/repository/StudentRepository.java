package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Admin;
import com.example.demo.model.Student;
@Repository
public interface StudentRepository extends MongoRepository<Student, String>{



	Optional<Student> findByEmail(String email);

	Optional<Student> findByName(String name);
}
