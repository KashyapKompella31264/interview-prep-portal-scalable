package com.example.demo.controller;

import com.example.demo.model.Course;
import com.example.demo.service.CourseService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    // 1️⃣ Create a Course (Admin only)
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Course> createCourse(@RequestBody Course course) {
        Course createdCourse = courseService.createCourse(course);
        return ResponseEntity.ok(createdCourse);
    }

    // 2️⃣ Get all Courses (Accessible to all)
    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {
        List<Course> courses = courseService.getAllCourses();
        return ResponseEntity.ok(courses);
    }

    // 3️⃣ Get a Course by ID (Accessible to all)
    @GetMapping("/{courseId}")
    public ResponseEntity<Course> getCourseById(@PathVariable String courseId) {
        Course course = courseService.getCourseById(courseId);
        return course != null ? ResponseEntity.ok(course) : ResponseEntity.notFound().build();
    }

    // 4️⃣ Update a Course (Admin only)
    @PutMapping("/{courseId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Course> updateCourse(@PathVariable String courseId, @RequestBody Course updatedCourse) {
        Course course = courseService.updateCourse(courseId, updatedCourse);
        return course != null ? ResponseEntity.ok(course) : ResponseEntity.notFound().build();
    }

    // 5️⃣ Delete a Course (Admin only)
    @DeleteMapping("/{courseId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCourse(@PathVariable String courseId) {
        boolean deleted = courseService.deleteCourse(courseId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    // 6️⃣ Assign an Instructor to a Course (Admin only)
    @PostMapping("/{courseId}/assign-instructor/{instructorId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Course> assignInstructorToCourse(@PathVariable String courseId, @PathVariable String instructorId) {
        Course course = courseService.assignInstructor(courseId, instructorId);
        return course != null ? ResponseEntity.ok(course) : ResponseEntity.notFound().build();
    }

    // 7️⃣ Enroll a Student in a Course (Student can self-enroll)
    @PostMapping("/{courseId}/enroll/{studentId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Course> enrollStudent(@PathVariable String courseId, @PathVariable String studentId) {
        Course course = courseService.enrollStudent(courseId, studentId);
        return course != null ? ResponseEntity.ok(course) : ResponseEntity.notFound().build();
    }
}
