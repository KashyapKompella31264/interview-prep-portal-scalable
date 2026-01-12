package com.example.demo.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.model.Course;
import com.example.demo.model.Instructor;
import com.example.demo.model.Student;
import com.example.demo.security.JwtFilter;
import com.example.demo.service.AnnouncementsService;
import com.example.demo.service.CourseService;
import com.example.demo.service.InstructorService;
import com.example.demo.service.QuestionService;
import com.example.demo.service.StudentService;
import com.example.demo.service.UserService;


@WebMvcTest(
	    controllers = StudentController.class,
	    excludeFilters = {
	        @ComponentScan.Filter(
	            type = FilterType.ASSIGNABLE_TYPE,
	            classes = JwtFilter.class
	        )
	    }
	)
	@Import(TestSecurityConfig.class)
public class StudentControllerTest {
	@Autowired
	private MockMvc mockmvc;
	@MockBean private StudentService studentService;
	@MockBean private UserService userService;
	@MockBean private InstructorService instructorService;
	@MockBean private CourseService courseService;
	@MockBean private QuestionService questionService;
	@MockBean private AnnouncementsService announcementsService;
	@Test
	@WithMockUser(roles= "STUDENT")
	void testGetStudentById() throws Exception{
		Student s=new Student();
		s.setId("5000");
		s.setName("Kashyap");
		when(studentService.findStudentById("5000")).thenReturn(Optional.of(s));
		mockmvc.perform(get("/student/5000")).andExpect(status().isOk()).andExpect(jsonPath("$.name").value("Kashyap"));
	}
	
	@Test
	@WithMockUser(roles="STUDENT")
	void testGetInstructorById() throws Exception{
		Instructor i=new Instructor();
		i.setId("2501");
		i.setName("Srikar");
		when(instructorService.getInstructorById("2501")).thenReturn(Optional.of(i));
		mockmvc.perform(get("/student/getinstructor/2501")).andExpect(status().isOk()).andExpect(jsonPath("$.name").value("Srikar"));
		
	}
	
	@Test
	@WithMockUser(roles="STUDENT")
	void testGetCoursesByStudentId() throws Exception{
		when(courseService.findCourseByStudentId("5000")).thenReturn(Arrays.asList(new Course(), new Course()));
		mockmvc.perform(get("/student/5000/courses")).andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(2));
	}
	
}
