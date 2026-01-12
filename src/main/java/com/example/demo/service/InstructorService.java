package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.model.Instructor;
import com.example.demo.model.User;
import com.example.demo.repository.InstructorRepository;

@Service
public class InstructorService {
    private final InstructorRepository instructorRepository;

    public InstructorService(InstructorRepository instructorRepository) {
        this.instructorRepository = instructorRepository;
    }

    public Instructor saveInstructor(Instructor instructor) {
        return instructorRepository.save(instructor);
    }

    public List<Instructor> getAllInstructors() {
        return instructorRepository.findAll();
    }

    public Optional<Instructor> getInstructorById(String id) {
        return instructorRepository.findById(id);
    }

    public Instructor updateInstructor(String id, Instructor updatedInstructor) {
        return instructorRepository.findById(id).map(instructor -> {
            instructor.setName(updatedInstructor.getName());
            instructor.setPassword(updatedInstructor.getPassword());
            instructor.setEmail(updatedInstructor.getEmail());
            instructor.setExperience(updatedInstructor.getExperience());
            instructor.setExpertise(updatedInstructor.getExpertise());
            return instructorRepository.save(instructor);
        }).orElseThrow();
    }

    public void deleteInstructor(String id) {
        instructorRepository.deleteById(id);
    }

	public Optional<Instructor> findByEmail(String email) {
		return instructorRepository.findByEmail(email);
	}

	public Optional<User> findByName(String name) {
		return instructorRepository.findByName(name);
	}
	public List<Instructor> getInstructorsByIds(List<String> instructorIds) {
        return instructorRepository.findByIdIn(instructorIds);
    }
}
