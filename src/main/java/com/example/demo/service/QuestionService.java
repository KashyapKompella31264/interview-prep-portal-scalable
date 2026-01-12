package com.example.demo.service;

import com.example.demo.model.Question;
import com.example.demo.model.SubTopic;
import com.example.demo.model.Course;
import com.example.demo.model.Instructor;
import com.example.demo.repository.QuestionRepository;
import com.example.demo.repository.InstructorRepository;
import com.example.demo.repository.AdminRepository;
import com.example.demo.repository.CourseRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final CourseRepository courseRepository;
    
    public QuestionService(QuestionRepository questionRepository,CourseRepository courseRepository) {
        this.questionRepository = questionRepository;
        this.courseRepository=courseRepository;
    }

    public Question saveQuestion(Question question) {
        return questionRepository.save(question);
    }

    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    public Optional<Question> getQuestionById(String id) {
        return questionRepository.findById(id);
    }

    public Question updateQuestion(String id, Question updatedQuestion) {
        return questionRepository.findById(id).map(question -> {
            question.setTitle(updatedQuestion.getTitle());
            question.setDescription(updatedQuestion.getDescription());
            question.setTestcases(updatedQuestion.getTestcases());
            return questionRepository.save(question);
        }).orElseThrow(() -> new RuntimeException("Question not found"));
    }

    public void deleteQuestion(String id) {
        questionRepository.deleteById(id);
    }
//    public List<Question> getQuestionsnotinSubTopic(String subTopicId){
//    	SubTopic subTopic=courseService.getSubTopicById(subTopicId);
//    	List<String> questionids=subTopic.getQuestions();
//    	if(questionids==null||questionids.isEmpty()) {
//    		return questionRepository.findAll();
//    	}
//    	return questionRepository.findByIdNotIn(questionids);
//    }
}