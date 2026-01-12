package com.example.demo.controller;

import com.example.demo.model.Course;
import com.example.demo.model.Instructor;
import com.example.demo.model.Role;
import com.example.demo.model.Student;
import com.example.demo.model.SubTopic;
import com.example.demo.model.User;
import com.example.demo.service.AnnouncementsService;
import com.example.demo.service.CourseService;
import com.example.demo.service.InstructorService;
import com.example.demo.service.QuestionService;
import com.example.demo.service.StudentService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/student")
@PreAuthorize("hasRole('STUDENT')")
public class StudentController {
    @Autowired
    private UserService userService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private  CourseService courseService;
    @Autowired
    private InstructorService instructorService;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private AnnouncementsService announcementsService;
    // ✅ READ User by ID
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Student>> getUserById(@PathVariable String id) {
    	Optional<Student> students=studentService.findStudentById(id);
    	if(students==null) {
    		return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    	}
        return ResponseEntity.ok(students);
    }
    // ✅ UPDATE User
    @PutMapping("/{id}")
    public ResponseEntity<Student> updateUser(@PathVariable String id, @RequestBody Student student) { // Ensure role is USER
    	Student newstudent=studentService.updateStudent(id, student);
    	User updatedUser=new User(id,newstudent.getName(),newstudent.getEmail(),newstudent.getPassword(),"STUDENT");
    	userService.updateUser(id, updatedUser);
        return ResponseEntity.ok(newstudent);
    }
    @PutMapping("/updatestudent/{studentId}")
    public ResponseEntity<?> updateStudent(@PathVariable String studentId,@RequestBody Student student){
    	Student s=studentService.updateStudent(studentId, student);
    	if(s==null) {
    		return (ResponseEntity<?>) ResponseEntity.notFound();
    	}
    	return ResponseEntity.ok(s);
    }
    @GetMapping("/courses")
    public ResponseEntity<?> getAllCourses() {
        List<Course> courses = courseService.getAllCourses();
        
        List<Map<String, Object>> courseList = courses.stream().map(course -> {
            Map<String, Object> courseResponse = new HashMap<>();
            courseResponse.put("id", course.getId());
            courseResponse.put("title", course.getTitle());
            courseResponse.put("description", course.getDescription());
            courseResponse.put("instructors", instructorService.getInstructorsByIds(course.getInstructors())); // ✅ Fetch full instructor details
            courseResponse.put("subTopics", course.getSubTopics());
            return courseResponse;
        }).toList();

        return ResponseEntity.ok(courseList);
    }

    @GetMapping("/courses/{id}")
    public ResponseEntity<?> getCourse(@PathVariable String id) {
        Optional<Course> optionalCourse = Optional.ofNullable(courseService.getCourseById(id));

        if (!optionalCourse.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", "Course not found"));
        }

        Course course = optionalCourse.get();

        Map<String, Object> response = new HashMap<>();
        response.put("id", course.getId());
        response.put("title", course.getTitle());
        response.put("description", course.getDescription());
        response.put("instructors", instructorService.getInstructorsByIds(course.getInstructors())); // ✅ Fetch full instructor details
        response.put("subTopics", course.getSubTopics());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/courses/{courseid}/registercourse/{studentid}")
    public ResponseEntity<Map<String, Object>> registerforaCourse(@PathVariable String courseid,@PathVariable String studentid){
    	System.out.println("Received request: courseId=" + courseid + ", studentId=" + studentid);
    	Course course=courseService.enrollStudent(courseid, studentid);
    	Map<String,Object> response=new HashMap<>();
    	if (course != null) {
            System.out.println("Enrollment successful!");
            response.put("message", "Enrollment successful!");
            response.put("course", course);
            return ResponseEntity.ok(response);
        } else {
        	System.out.println("Enrollment failed: Already enrolled or Course not found");
            response.put("error", "Student already enrolled or course not found");
            response.put("error", "Course not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
    @GetMapping("/{studentId}/courses")
    public ResponseEntity<List<Course>> getCourseByStudentId(@PathVariable String studentId) {
        List<Course> courses=courseService.findCourseByStudentId(studentId);
        return ResponseEntity.ok(courses);
    }
    @GetMapping("/getinstructor/{instructorId}")
    public ResponseEntity<?> getinstructorById(@PathVariable String instructorId){
    	Optional<Instructor> instructor=instructorService.getInstructorById(instructorId);
    	if(instructor.isPresent()) {
    		return ResponseEntity.ok(instructor.get());
    	}else {
    		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Instructor not found with id: "+instructorId);
    	}
    }
    @GetMapping("/getsubtopic/{subtopicId}")
    public ResponseEntity<?> getSubTopicById(@PathVariable String subtopicId){
    	SubTopic subtopic=courseService.getSubTopicById(subtopicId);
    	return ResponseEntity.ok(subtopic);
    }
    @GetMapping("/getquestion/{questionId}")
    public ResponseEntity<?> getQuestionById(@PathVariable String questionId){
    	return ResponseEntity.ok(questionService.getQuestionById(questionId));
    }
    @PostMapping("/getannouncements/course/{courseId}")
    public ResponseEntity<?> getAnnouncementsOfaCourse(@PathVariable String courseId){
    	return ResponseEntity.ok(announcementsService.getAnnouncementsforaCourse(courseId));
    }
}
