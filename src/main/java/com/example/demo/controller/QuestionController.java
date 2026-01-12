package com.example.demo.controller;

import com.example.demo.model.Question;
import com.example.demo.service.QuestionService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/questions")
public class QuestionController {
    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }
    @PostMapping("/questions")
	public ResponseEntity<Question> createQuestion(@RequestBody Question question){
		return ResponseEntity.ok(questionService.saveQuestion(question));
	}
	@GetMapping("/questions")
	public ResponseEntity<List<Question>> getAllQuestions(){
		return ResponseEntity.ok(questionService.getAllQuestions());
	}
	@PutMapping("/questions/{id}")
    public ResponseEntity<Question> updateQuestion(@PathVariable String id, @RequestBody Question question) {
        return ResponseEntity.ok(questionService.updateQuestion(id, question));
    }

    @DeleteMapping("/questions/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable String id) {
        questionService.deleteQuestion(id);
        return ResponseEntity.noContent().build();
    }
}
