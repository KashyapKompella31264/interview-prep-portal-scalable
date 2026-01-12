package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.model.Admin;
import com.example.demo.model.Student;
import com.example.demo.model.User;
import com.example.demo.repository.StudentRepository;

@Service
public class StudentService {
	@Autowired
	private StudentRepository studentRepository;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
//	public StudentService(StudentRepository studentRepository) {
//		this.studentRepository=studentRepository;
//	}
	public Optional<Student> findStudentById(String id) {
        return studentRepository.findById(id);
    }
	public Optional<Student> findStudentByEmail(String email) {
        return studentRepository.findByEmail(email);
    }
	public Student createStudent(Student student) {
		return studentRepository.save(student);
	}
	public List<Student> getAllStudents() {
		return studentRepository.findAll();
	}
	public String deletestudent(String studentid) {
		studentRepository.deleteById(studentid);
		return "deleted Successfully";
	}

	public Student updateStudent(String id, Student updatedStudent) {
	    return studentRepository.findById(id).map(student -> {
	        student.setName(updatedStudent.getName());
	        student.setEmail(updatedStudent.getEmail());
	        student.setAddress(updatedStudent.getAddress());
	        student.setPhno(updatedStudent.getPhno());
	        student.setAge(updatedStudent.getAge());

	        // Only encode and update password if it's not null or empty
	        if (updatedStudent.getPassword() != null && !updatedStudent.getPassword().isBlank()) {
	            String encodedPassword = passwordEncoder.encode(updatedStudent.getPassword());
	            student.setPassword(encodedPassword);
	        }
	        return studentRepository.save(student);
	    }).orElseThrow(() -> new RuntimeException("Student not found"));
	}

}
