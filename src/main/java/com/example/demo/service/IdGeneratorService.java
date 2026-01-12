package com.example.demo.service;

import com.example.demo.model.IdSequence;
import com.example.demo.repository.IdSequenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class IdGeneratorService {

    @Autowired
    private IdSequenceRepository idSequenceRepository;

    public synchronized int generateId(String entityType) {
        int min, max;

        switch (entityType.toUpperCase()) {
            case "QUESTION":
                min = 1;
                max = 2000;
                break;
            case "ADMIN":
                min = 3500;
                max = 4000;
                break;
            case "INSTRUCTOR":
                min = 2501;
                max = 3500;
                break;
            case "STUDENT":
                min = 5000;
                max = 7000;
                break;
            case "COURSE":
            	min = 10000;
            	max = 10500;
            	break;
            case "SUBTOPIC":
            	min = 10501;
            	max = 20000;
            	break;
            case "ANNOUNCEMENTS":
            	min = 50000;
            	max = 70000;
            	break;
            default:
                throw new IllegalArgumentException("Invalid entity type: " + entityType);
        }

        Optional<IdSequence> sequence = idSequenceRepository.findById(entityType);
        IdSequence idSequence;

        if (sequence.isPresent()) {
            idSequence = sequence.get();
            if (idSequence.getLastAssignedId() >= max) {
                throw new RuntimeException("ID range exceeded for entity: " + entityType);
            }
            idSequence.setLastAssignedId(idSequence.getLastAssignedId() + 1);
        } else {
            idSequence = new IdSequence(entityType, min);
        }

        idSequenceRepository.save(idSequence);
        return idSequence.getLastAssignedId();
    }
}
