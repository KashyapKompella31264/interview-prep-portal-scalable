package com.example.demo.controller;

import com.example.demo.model.Announcements;
import com.example.demo.model.Course;
import com.example.demo.model.Instructor;
import com.example.demo.model.Question;
import com.example.demo.model.SubTopic;
import com.example.demo.model.User;
import com.example.demo.repository.SubTopicRepository;
import com.example.demo.service.AnnouncementsService;
import com.example.demo.service.CourseService;
import com.example.demo.service.IdGeneratorService;
import com.example.demo.service.InstructorService;
import com.example.demo.service.QuestionService;
import com.example.demo.service.StudentService;
import com.example.demo.service.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

//import java.lang.foreign.Linker.Option;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/instructors")
@PreAuthorize("hasRole('INSTRUCTOR')")
public class InstructorController {
    private final InstructorService instructorService;
    private final QuestionService questionService;
    private final UserService userService;
    private final IdGeneratorService idGeneratorService;
    private final CourseService courseService;
    private final SubTopicRepository subTopicRepository;
    private final StudentService studentService;
    private final AnnouncementsService announcementsService;
    public InstructorController(InstructorService instructorService, QuestionService questionService,UserService userService ,
    		IdGeneratorService idGeneratorService,CourseService courseService,SubTopicRepository subTopicRepository,
    		StudentService studentService,AnnouncementsService announcementsService) {
        this.instructorService = instructorService;
        this.questionService = questionService;
        this.userService=userService;
        this.idGeneratorService=idGeneratorService;
        this.courseService=courseService;
        this.subTopicRepository=subTopicRepository;
        this.studentService=studentService;
        this.announcementsService=announcementsService;
    }
//    @PostMapping("/")
//	public ResponseEntity<?> createInstructor(@RequestBody Instructor instructor){
//    	int generatedid=idGeneratorService.generateId("INSTRUCTOR");
//        if (userService.findByEmail(instructor.getEmail()).isPresent()) {
//            return ResponseEntity.badRequest().body("Error: Email is already taken!");
//        }
//        if (instructorService.findByName(instructor.getName()).isPresent()) {
//            return ResponseEntity.badRequest().body("Error: Email is already associated with an instructor!");
//        }
//        
//    	User userinstructor=new User(String.valueOf(generatedid),instructor.getName(),instructor.getEmail(), instructor.getPassword(),"INSTRUCTOR");
//    	if (userService.findByUsername(instructor.getName()).isPresent()) {
//            return ResponseEntity.badRequest().body("Error: Email is already associated with an user!");
//        }
//    	if (userService.findByEmail(instructor.getName()).isPresent()) {
//            return ResponseEntity.badRequest().body("Error: Email is already associated with an user!");
//        }
//    	userService.registerUser(userinstructor);
// 
//		return ResponseEntity.ok(instructorService.saveInstructor(instructor));
//	}
//	@GetMapping("/")
//	public ResponseEntity<List<Instructor>> getAllInstructors(){
//		return ResponseEntity.ok(instructorService.getAllInstructors());
//	}
    @PostMapping("/{instructorId}")
    public ResponseEntity<?> getInstructorById(@PathVariable String instructorId){
    	Optional<Instructor> i=instructorService.getInstructorById(instructorId);
    	if(!i.isPresent()) {
    		throw new RuntimeException("Instructor not found");
    	}
    	Instructor instructor=i.get();
    	return ResponseEntity.ok(instructor);
    }
	@PutMapping("/{id}")
    public ResponseEntity<Instructor> updateInstructor(@PathVariable String id, @RequestBody Instructor instructor) {
        return ResponseEntity.ok(instructorService.updateInstructor(id, instructor));
    }
	@GetMapping("/student/getallstudents")
	public ResponseEntity<?> getallstudents(){
		return ResponseEntity.ok(studentService.getAllStudents());
	}
	@GetMapping("/student/{studentId}")
	public ResponseEntity<?> getStudentById(@PathVariable String studentId){
		return ResponseEntity.ok(studentService.findStudentById(studentId));
	}
    @PostMapping("/questions")
    public ResponseEntity<Question> createQuestion(@RequestBody Question question) {
    	int id=idGeneratorService.generateId("QUESTION");
    	question.setId(String.valueOf(id));
        return ResponseEntity.ok(questionService.saveQuestion(question));
    }
    @GetMapping("/questions")
    public ResponseEntity<List<Question>> getAllQuestions() {
        return ResponseEntity.ok(questionService.getAllQuestions());
    }
    @GetMapping("/courses/{courseId}")
    public ResponseEntity<?> getCourseById(@PathVariable String courseId){
    	return ResponseEntity.ok(courseService.getCourseById(courseId));
    }
    @PostMapping("/subtopic/{subTopicId}/question/{questionId}")
    public ResponseEntity<?> addaQuestionToSubTopic(@PathVariable String subTopicId,@PathVariable String questionId){
    	SubTopic subTopic=courseService.getSubTopicById(subTopicId);
    	Optional<Question> q=questionService.getQuestionById(questionId);
    	if(subTopic==null || !q.isPresent()) {
    		throw new RuntimeException("Course or Question is not found");
    	}
    	if(subTopic.getQuestions()==null) {
    		subTopic.setQuestions(new ArrayList<String>());
    	}
    	List<String> questions=subTopic.getQuestions();
    	questions.add(questionId);
    	subTopic.setQuestions(questions);
    	subTopicRepository.save(subTopic);
    	return ResponseEntity.ok(subTopic);
    }
    @PutMapping("/question/{questionId}")
    public ResponseEntity<?> updateQuestion(@PathVariable String questionId,@RequestBody Question question){
    	return ResponseEntity.ok(questionService.updateQuestion(questionId, question));
    }
    @PostMapping("/getinstructorcourses/{instructorId}")
    public ResponseEntity<?> getcoursesfInstructor(@PathVariable String instructorId){
    	return ResponseEntity.ok(courseService.getcoursesByInstructorId(instructorId));
    }
    @GetMapping("/subtopic/{subtopicId}")
    public ResponseEntity<?> getsubtopicById(@PathVariable String subtopicId){
    	return ResponseEntity.ok(courseService.getSubTopicById(subtopicId));
    }
    @GetMapping("/questions/{questionId}")
    public ResponseEntity<?> getQuestionById(@PathVariable String questionId){
    	return ResponseEntity.ok(questionService.getQuestionById(questionId));
    }
    @PutMapping("/courses/{coursesId}")
    public ResponseEntity<?> updatecourse(@PathVariable String courseId,@RequestBody Course updatedcourse){
    	return ResponseEntity.ok(courseService.updateCourse(courseId, updatedcourse));
    }
    @GetMapping("/course/{courseId}/getstudents")
    public ResponseEntity<?> getstudentsofacourse(@PathVariable String courseId){
    	return ResponseEntity.ok(courseService.getstudentsofacourse(courseId));
    }
    @PostMapping("/announcements/addannouncement")
    public ResponseEntity<?> postanAnnouncement(@RequestBody Announcements announcement){
    	int id=idGeneratorService.generateId("ANNOUNCEMENTS");
    	announcement.setId(String.valueOf(id));
    	announcement.setCreatedAt(LocalDateTime.now());
    	return ResponseEntity.ok(announcementsService.addAnnouncement(announcement));
    }
    @GetMapping("/announcements/{courseId}")
    public ResponseEntity<?> getAnnouncementsForCourse(@PathVariable String courseId) {
        return ResponseEntity.ok(announcementsService.getAnnouncementsforaCourse(courseId));
    }
}
	