package com.example.demo.service;

import com.example.demo.model.Course;
import com.example.demo.model.Instructor;
import com.example.demo.model.Question;
import com.example.demo.model.Student;
import com.example.demo.model.SubTopic;
import com.example.demo.model.User;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.InstructorRepository;
import com.example.demo.repository.StudentRepository;
import com.example.demo.repository.SubTopicRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository; // For assigning instructors & enrolling students
    private final StudentRepository studentRepository;
    private final InstructorRepository instructorRepository;
    private final IdGeneratorService idGeneratorService;
    private final SubTopicRepository subTopicRepository;
    private final QuestionService questionService;
    public CourseService(CourseRepository courseRepository, UserRepository userRepository, InstructorRepository instructorRepository,
    		StudentRepository studentRepository,IdGeneratorService idGeneratorService,
    		SubTopicRepository subTopicRepository,QuestionService questionService) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
		this.instructorRepository = instructorRepository;
		this.studentRepository=studentRepository;
		this.idGeneratorService=idGeneratorService;
		this.subTopicRepository=subTopicRepository;
		this.questionService=questionService;
    }

    // 1️⃣ Create a Course (Admin only)
    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }

    // 2️⃣ Get all Courses
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    // 3️⃣ Get a Course by ID
    public Course getCourseById(String courseId) {
        return courseRepository.findById(courseId).orElse(null);
    }

    // 4️⃣ Update a Course (Admin only)
    public Course updateCourse(String courseId, Course updatedCourse) {
        Optional<Course> existingCourseOpt = courseRepository.findById(courseId);
        if (existingCourseOpt.isPresent()) {
            Course existingCourse = existingCourseOpt.get();
            existingCourse.setTitle(updatedCourse.getTitle());
            existingCourse.setDescription(updatedCourse.getDescription());
            existingCourse.setInstructors(updatedCourse.getInstructors());
            existingCourse.setSubTopics(updatedCourse.getSubTopics());
            return courseRepository.save(existingCourse);
        }
        return null;
    }

    // 5️⃣ Delete a Course (Admin only)
    public boolean deleteCourse(String courseId) {
        if (courseRepository.existsById(courseId)) {
            courseRepository.deleteById(courseId);
            return true;
        }
        return false;
    }

    // 6️⃣ Assign an Instructor to a Course (Admin only)
    public Course assignInstructor(String courseId, String instructorId) {
        Optional<Course> courseOpt = courseRepository.findById(courseId);
        Optional<Instructor> instructorOpt = instructorRepository.findById(instructorId);

        if (courseOpt.isPresent() && instructorOpt.isPresent()) {
            Course course = courseOpt.get();
            // Ensure instructors list is initialized
            if (course.getInstructors() == null) {
                course.setInstructors(new ArrayList<>());
            }

            course.getInstructors().add(instructorId); // Store instructor ID instead of User object
            return courseRepository.save(course);
        }
        return null;
    }



    // 7️⃣ Enroll a Student in a Course (Student can self-enroll)
    public Course enrollStudent(String courseId, String studentId) {
        Optional<Course> courseOpt = courseRepository.findById(courseId);
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        
        if (courseOpt.isPresent() && studentOpt.isPresent() ) {
            Course course = courseOpt.get();
            // Ensure registeredStudents is initialized
            if(course.getRegisteredStudents().contains(studentId)) {
            	return null;
            }
            if (course.getRegisteredStudents() == null) {
                course.setRegisteredStudents(new ArrayList<>());
            }

            course.getRegisteredStudents().add(studentId); // Add student ID instead of User object
            return courseRepository.save(course);
        }
        return null;
    }
    public Course addSubTopictoCourse(String courseid,String subtopicId) {
    	Optional<Course> Optcourse=courseRepository.findById(courseid);
    	Course course=Optcourse.get();
    	course.getSubTopics().add(subtopicId);
    	courseRepository.save(course);
    	return course;
    }
    public List<Course> findCourseByStudentId(String studentId){
    	return courseRepository.findCoursesByRegisteredStudentsContaining(studentId);
    }
    public SubTopic crerateanewSubTopic(SubTopic subtopic) {
    	int id=idGeneratorService.generateId("SUBTOPIC");
    	subtopic.setId(String.valueOf(id));
    	subTopicRepository.save(subtopic);
    	return subtopic;
    }
    public String crerateaNewSubTopic(SubTopic subtopic) {
    	int id=idGeneratorService.generateId("SUBTOPIC");
    	subtopic.setId(String.valueOf(id));
    	subTopicRepository.save(subtopic);
    	return String.valueOf(id);
    }
    public SubTopic getSubTopicById(String StudTopicId) {
    	return subTopicRepository.findById(StudTopicId).orElse(null);
    }
    public List<SubTopic> getallSubTopics(){
    	return subTopicRepository.findAll();
    }
    public SubTopic setquestiontoSubTopic(SubTopic subtopic,String questionId) {
    	if(subtopic.getQuestions()==null) {
    		subtopic.setQuestions(new ArrayList<>());
    	}
    	subtopic.getQuestions().add(questionId);
    	subTopicRepository.save(subtopic);
    	return subtopic;
    }
    public List<SubTopic> getallsubtopicsforacourse(String courseId){
    	Optional<Course> c=courseRepository.findById(courseId);
    	if(c!=null) {
    		Course course=c.get();
    		List<SubTopic> subtopics=subTopicRepository.findAllById(course.getSubTopics());
    		return subtopics;
    	}
    	return null;
    }
    public void deletesubtopicById(String subtopicId) {
    	if(subTopicRepository.findById(subtopicId)!=null) {
    		subTopicRepository.deleteById(subtopicId);
    	}
    }
    public SubTopic updateSubTopic(SubTopic subtopic) {
    	return subTopicRepository.save(subtopic);
    }
    public String removequestionfromsubtopic(String subtopicId,String questionId) {
    	Optional<SubTopic> s=subTopicRepository.findById(subtopicId);
    	Optional<Question> q=questionService.getQuestionById(questionId);
    	if(s.isPresent()&&q.isPresent()) {
    		SubTopic subtopic=s.get();
    		Question question=q.get();
    		List<String> questions=subtopic.getQuestions();
    		if(questions.contains(questionId)) {
    			questions.remove(questionId);
    			subTopicRepository.save(subtopic);
    			return "Removed Successfully";
    		}
    	}else {
    		return "Question Id or SubTopic Id is invalid";
    	}
    	return null;
    }
    public List<Course> getcoursesByInstructorId(String instructorId){
    	return courseRepository.findCoursesByInstructorsContaining(instructorId);
    }
    public List<String> getstudentsofacourse(String courseId){
    	Optional<Course> c=courseRepository.findById(courseId);
    	if(c==null) {
    		return null;
    	}
    	else {
    		Course course=c.get();
    		List<String> students=course.getRegisteredStudents();
    		return students;
    	}
    }
}
