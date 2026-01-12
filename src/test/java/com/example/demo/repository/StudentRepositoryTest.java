package com.example.demo.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;

import com.example.demo.model.Student;

@DataMongoTest(excludeAutoConfiguration = {
        com.example.demo.config.SecurityConfig.class
})
@ActiveProfiles("test")

public class StudentRepositoryTest {
	@Autowired
	private StudentRepository studentRepository;
	@Test
	void testFindByEmailId() {
		Student s=new Student();
		s.setName("jeevan");
		s.setEmail("jeevan@gmail.com");
		studentRepository.save(s);
		Optional<Student> student=studentRepository.findByEmail("jeevan@gmail.com");
		assertTrue(student.isPresent());
		assertEquals("jeevan",student.get().getName());
	}
}