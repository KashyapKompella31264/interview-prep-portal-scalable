package com.example.demo.controller;

//import java.lang.foreign.Linker.Option;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Admin;
import com.example.demo.model.Course;
import com.example.demo.model.Instructor;
import com.example.demo.model.Question;
import com.example.demo.model.Role;
import com.example.demo.model.Student;
import com.example.demo.model.SubTopic;
import com.example.demo.model.TestCase;
import com.example.demo.model.User;
import com.example.demo.repository.QuestionRepository;
import com.example.demo.repository.StudentRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.AdminService;
import com.example.demo.service.CourseService;
import com.example.demo.service.IdGeneratorService;
import com.example.demo.service.InstructorService;
import com.example.demo.service.QuestionService;
import com.example.demo.service.StudentService;
import com.example.demo.service.UserService;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
	private final UserService userService;
	private final AdminService adminService;
	private final QuestionService questionService;
	private final InstructorService instructorService;
	private IdGeneratorService idGeneratorService;
	private StudentService studentService;
	private CourseService courseService;
	private UserRepository userRepository;
	private QuestionRepository questionRepository;
	private PasswordEncoder passwordEncoder;
	private StudentRepository studentRepository;
	public AdminController(UserService userService,AdminService adminService,QuestionService questionService, 
			InstructorService instructorService,IdGeneratorService idGeneratorService,StudentService studentService,
			CourseService courseService,QuestionRepository questionRepository,UserRepository userRepository,
			StudentRepository studentRepository,PasswordEncoder passwordEncoder
		) {
		this.userService=userService;
		this.adminService = adminService;
		this.questionService=questionService;
		this.instructorService = instructorService;
		this.idGeneratorService=idGeneratorService;
		this.studentService=studentService;
		this.courseService=courseService;
		this.questionRepository=questionRepository;
		this.userRepository=userRepository;
		this.studentRepository=studentRepository;
		this.passwordEncoder=passwordEncoder;
	}
	//ADMIN CRUD
	// ✅ CREATE Admin
    @PostMapping("/addadmin")
    public ResponseEntity<User> createAdmin(@RequestBody Admin admin) {
    	int generatedId = idGeneratorService.generateId("ADMIN");
        admin.setId(String.valueOf(generatedId));
        User adminUser = new User(admin.getId(),admin.getName(),admin.getEmail(),admin.getPassword(),"ADMIN");
        //adminUser.setRole("ADMIN");
        userService.registerUser(adminUser);
        adminService.saveAdmin(admin);
        
        return ResponseEntity.ok(adminUser);
    }
    // ✅ READ All Admins
    @GetMapping("/")
    public ResponseEntity<List<Admin>> getAllAdmins() {
        List<Admin> admins = adminService.getAllAdmins();
        return ResponseEntity.ok(admins);
    }
    // ✅ READ Admin by ID
    @GetMapping("/{id}")
    public ResponseEntity<Optional<User>> getAdminById(@PathVariable String id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }
    // ✅ UPDATE Admin
    @PutMapping("/{id}")
    public ResponseEntity<User> updateAdmin(@PathVariable String id, @RequestBody User updatedAdmin) {
        updatedAdmin.setRole("ADMIN"); // Ensure role is ADMIN
        return ResponseEntity.ok(userService.updateUser(id, updatedAdmin));
    }
    // ✅ DELETE Admin
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAdmin(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("Admin deleted successfully");
    }
	//CRUD ON STUDENTS
	@GetMapping("/getstudents")
	public ResponseEntity<List<Student>> getAllStudents(){
		System.out.println("getting data all students by admin authorization");
		List<Student> admins = studentService.getAllStudents();
        return ResponseEntity.ok(admins);
	}
	@DeleteMapping("/students/{id}")
	public ResponseEntity<String> deleteUser(@PathVariable String id){
		userService.deleteUser(id);
		return ResponseEntity.ok("user deleted successfully");
	}
	@PutMapping("/students/{id}")
    public ResponseEntity<User> updateUser(@PathVariable String id, @RequestBody User user) {
		
        return ResponseEntity.ok(userService.updateUser(id, user));
    }
	@PostMapping("/students")
	public ResponseEntity<?> createUser(@RequestBody Student student){
		if (studentService.findStudentById(student.getName()).isPresent()) {
	        return ResponseEntity.badRequest().body("Error: Student with this username already exists.");
	    }
		if (studentService.findStudentByEmail(student.getEmail()).isPresent()) {
	        return ResponseEntity.badRequest().body("Error: Student with this email already exists.");
	    }
		if (userService.findByUsername(student.getName()).isPresent()) {
	        return ResponseEntity.badRequest().body("Error: Student with this username already exists.");
	    }
		if (userService.findByEmail(student.getEmail()).isPresent()) {
	        return ResponseEntity.badRequest().body("Error: Student with this email already exists.");
	    }
		int generatedId = idGeneratorService.generateId("STUDENT");
		student.setId(String.valueOf(generatedId));
        User userstudent = new User(String.valueOf(generatedId), student.getName(), student.getEmail(),student.getPassword(),"STUDENT");
        userService.createUser(userstudent);
        
		return ResponseEntity.ok(studentService.createStudent(student));
	}
	@PostMapping("/student/{id}")
	public ResponseEntity<?> getstudentbyId(@PathVariable String id){
		return ResponseEntity.ok(studentService.findStudentById(id));
	}
	@PutMapping("/student/{id}")
	public ResponseEntity<?> updateStudent(@PathVariable String id,@RequestBody Student student){
		Optional<User> user=userService.getUserById(id);
		User u=user.get();
		if(u!=null) {
			u.setUsername(student.getName());
			u.setPassword(student.getPassword());
			u.setEmail(student.getEmail());
			u.setRole("STUDENT");
			userService.saveUser(u);
		}
		else {
			return (ResponseEntity<?>) ResponseEntity.notFound();
		}
		return ResponseEntity.ok(studentService.updateStudent(id, student));
	}
	@PostMapping("/student")
	public ResponseEntity<?> createstudent(@RequestBody Student student){
		int generateId=idGeneratorService.generateId("STUDENT");
		student.setId(String.valueOf(generateId));
		User user=new User(String.valueOf(generateId),student.getName(),student.getEmail(),student.getPassword(),"STUDENT");
		userService.createUser(user);
		System.out.println(student);
		return ResponseEntity.ok(studentService.createStudent(student));
	}
	@DeleteMapping("/student/{studentId}")
	public ResponseEntity<?> deleteStudent(@PathVariable String studentId){
		studentService.findStudentById(studentId);
		userService.getUserById(studentId);
		userService.deleteUser(studentId);
		
		return ResponseEntity.ok(studentService.deletestudent(studentId));
	}
	//CRUD ON QUESTIONS
	@PostMapping("/questions")
	public ResponseEntity<Question> createQuestion(@RequestBody Question question){
		int generatedId=idGeneratorService.generateId("QUESTION");
		question.setId(String.valueOf(generatedId));
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
    public ResponseEntity<String> deleteQuestion(@PathVariable String id) {
        questionService.deleteQuestion(id);
        return ResponseEntity.ok("Question Deleted successfully");
    }
	@PostMapping("/questions/{id}")
	public ResponseEntity<?> getQuestionById(@PathVariable String id){
		return ResponseEntity.ok(questionService.getQuestionById(id));
	}
	//CRUD ON INSTRUCTOR
	@PostMapping("/instructors")
	public ResponseEntity<Instructor> createInstructor(@RequestBody Instructor instructor){
		int generateId=idGeneratorService.generateId("INSTRUCTOR");
		instructor.setId(String.valueOf(generateId));
		User userInstructor=new User(String.valueOf(generateId),instructor.getName(),instructor.getEmail(),instructor.getPassword(),"INSTRUCTOR");
		userService.createUser(userInstructor);
		return ResponseEntity.ok(instructorService.saveInstructor(instructor));
	}
	@GetMapping("/instructors")
	public ResponseEntity<List<Instructor>> getAllInstructors(){
		return ResponseEntity.ok(instructorService.getAllInstructors());
	}
	@PutMapping("/instructors/{id}")
    public ResponseEntity<?> updateInstructor(@PathVariable String id, @RequestBody Instructor instructor) {
		String username=instructor.getName();
		String email=instructor.getEmail();
		String password=instructor.getPassword();
		Optional<User> u=userService.getUserById(id);
		User user=u.get();
		if(u!=null && instructor!=null) {
			user.setUsername(username);
			user.setEmail(email);
			user.setPassword(passwordEncoder.encode(password));
			userRepository.save(user);
		}else {
			return (ResponseEntity<?>) ResponseEntity.notFound();
		}
		return ResponseEntity.ok(instructorService.updateInstructor(id, instructor));
    }

    @DeleteMapping("/instructors/{id}")
    public ResponseEntity<Void> deleteInstructor(@PathVariable String id) {
        instructorService.deleteInstructor(id);
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/instructor/{id}")
    public ResponseEntity<?> getInstructorById(@PathVariable String id){
    	Optional<Instructor> i=instructorService.getInstructorById(id);
    	Instructor instructor=i.get();
    	return ResponseEntity.ok(instructor);
    }
    
 // Course Management (Admin Only)
    @PostMapping("/courses")
    public ResponseEntity<Course> createCourse(@RequestBody Course course) {
    	int generatedId=idGeneratorService.generateId("COURSE");
    	System.out.println("Course id generated :"+generatedId);
    	course.setId(String.valueOf(generatedId));
        Course createdCourse = courseService.createCourse(course);
        return ResponseEntity.ok(createdCourse);
    }
    @PostMapping("/subTopic")
    public ResponseEntity<SubTopic> createsubTopic(@RequestBody SubTopic subTopic){
    	return ResponseEntity.ok(courseService.crerateanewSubTopic(subTopic));
    }
    @GetMapping("/courses")
    public ResponseEntity<?> getAllCourses() {
    	List<Course> courses=courseService.getAllCourses();
    	if(courses==null) {
    		return ResponseEntity.ok("NULL");
    	}
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/courses/{courseId}")
    public ResponseEntity<?> getCourseById(@PathVariable String courseId) {
        Optional<Course> optionalCourse = Optional.ofNullable(courseService.getCourseById(courseId));

        if (!optionalCourse.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found");
        }

        Course course = optionalCourse.get();

        // Extract instructor IDs from Instructor objects

        List<String> instructors = course.getInstructors();

        Map<String, Object> response = new HashMap<>();
        response.put("id", course.getId());
        response.put("title", course.getTitle());
        response.put("description", course.getDescription());
        response.put("instructors", instructorService.getInstructorsByIds(instructors)); // ✅ Sending full instructor details
        response.put("subTopics", course.getSubTopics());
        return ResponseEntity.ok(response);
    }
    @PutMapping("/courses/{courseId}")
    public ResponseEntity<Course> updateCourse(@PathVariable String courseId, @RequestBody Course updatedCourse) {
        Course course = courseService.updateCourse(courseId, updatedCourse);
        return course != null ? ResponseEntity.ok(course) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/courses/{courseId}")
    public ResponseEntity<Void> deleteCourse(@PathVariable String courseId) {
        boolean deleted = courseService.deleteCourse(courseId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
    @PostMapping("/courses/{courseId}/assign-instructor/{instructorId}")
    public ResponseEntity<Course> assignInstructorToCourse(@PathVariable String courseId, @PathVariable String instructorId) {
        Course course = courseService.assignInstructor(courseId, instructorId);
        return course != null ? ResponseEntity.ok(course) : ResponseEntity.notFound().build();
    }
    @PostMapping("/courses/{courseId}/assign-subtopic")
    public ResponseEntity<?> addSubTopictoCourse(@PathVariable String courseId,@RequestBody SubTopic subtopic) {
        Course course=courseService.getCourseById(courseId);
        String id=courseService.crerateaNewSubTopic(subtopic);
        if(course==null) return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Error: Course not found.");
        return ResponseEntity.ok(courseService.addSubTopictoCourse(courseId, id));
    }
    @PostMapping("/subtopic/{subtopicId}/questions/{questionId}")
    public ResponseEntity<?> addquestiontopsubTopic(@PathVariable String subtopicId,@PathVariable String questionId){
    	SubTopic subtopic=courseService.getSubTopicById(subtopicId);
    	if(subtopic!=null) {
    		return ResponseEntity.ok(courseService.setquestiontoSubTopic(subtopic, questionId));
    	}
    	return ResponseEntity.notFound().build();
    }
    @GetMapping("/getsubtopics")
    public ResponseEntity<?> getallSubtopics(){
    	List<SubTopic> sub=courseService.getallSubTopics();
    	
    	if(sub!=null) {
    		return ResponseEntity.ok(sub);
    	}else {
    		return ResponseEntity.ok(new ArrayList<>());
    	}
    }
    @PostMapping("/{questionId}/addTestCases")
    public ResponseEntity<?> addTestCasestoaQuestion(@PathVariable String questionId,@RequestBody List<TestCase> testcases){
    	Optional<Question> optquestion=questionService.getQuestionById(questionId);
    	if(!optquestion.isPresent()) {
    		throw new RuntimeException("Question not found with id: "+questionId);
    	}
    	Question question=optquestion.get();
    	if(question.getTestcases()==null) {
    		question.setTestcases(new ArrayList<>());
    	}
    	question.getTestcases().addAll(testcases);
    	return ResponseEntity.ok(questionRepository.save(question));
    }
    @PostMapping("/subtopic/{subtopicId}")
    public ResponseEntity<?> getSubtopicById(@PathVariable String subtopicId){
    	return ResponseEntity.ok(courseService.getSubTopicById(subtopicId));
    }
    @DeleteMapping("/subtopic/{subtopicId}")
    public String deletesubtopicById(@PathVariable String subtopicId){
    	SubTopic subtopic=courseService.getSubTopicById(subtopicId);
    	if(subtopic!=null) {
    		courseService.deletesubtopicById(subtopicId);
    		return "Deleted Successsfully";
    	}
    	else {
    		return "Not found";
    	}
    }
    @PutMapping("/subtopic/{subtopicId}")
    public ResponseEntity<?> updateSubTopic(@PathVariable String subtopicId,@RequestBody SubTopic updatedsubtopic){
    	SubTopic subTopic=courseService.getSubTopicById(subtopicId);
    	subTopic.setTitle(updatedsubtopic.getTitle());
    	if(subTopic.getQuestions()!=null) {
    		subTopic.setQuestions(updatedsubtopic.getQuestions());
    	}else {
    		subTopic.setQuestions(new ArrayList<>());
    	}
    	
    	return ResponseEntity.ok(courseService.updateSubTopic(subTopic));
    }
    
    @GetMapping("/courses/{courseId}/subtopics")
    public ResponseEntity<?> getallsubtopicsforacourse(@PathVariable String courseId){
    	return ResponseEntity.ok(courseService.getallsubtopicsforacourse(courseId));
    }
    @PostMapping("/removequestionfromsubtopic/{subtopicId}/{questionId}")
    public ResponseEntity<?> removequestionfromsubtopic(@PathVariable String subtopicId,@PathVariable String questionId){
    	return ResponseEntity.ok(courseService.removequestionfromsubtopic(subtopicId, questionId));
    }
    @PostMapping("/testcase/delete/{questionId}")
    public ResponseEntity<?> deletetestcaseofaquestion(@PathVariable String questionId,@RequestBody TestCase testcase){
    	Optional<Question> q=questionService.getQuestionById(questionId);
    	if(!q.isPresent()) {
    		throw new RuntimeException("Question not found");
    	}
    	Question question=q.get();
    	if(question.getTestcases()==null||question.getTestcases().isEmpty()) {
    		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No testcases found for the question");
    	}
    	boolean removed=question.getTestcases().removeIf(tc->
    	tc.getInput().equals(testcase.getInput()) &&
    	tc.getExpectedOutput().equals(testcase.getExpectedOutput()));
    	if (removed) {
    		questionRepository.save(question);
    		return ResponseEntity.ok("testcase Removed");
    	}else {
    		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Testcase Not Found");
    	}
    }
}