package com.example.demo.controller;

import com.example.demo.dto.LoginRequest;
import com.example.demo.model.Admin;
import com.example.demo.model.Instructor;
import com.example.demo.model.Student;
import com.example.demo.model.User;
import com.example.demo.security.JwtUtil;
import com.example.demo.service.AdminService;
import com.example.demo.service.InstructorService;
import com.example.demo.service.IdGeneratorService;
import com.example.demo.service.StudentService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private IdGeneratorService idGeneratorService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private UserService userService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private InstructorService instructorService;
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody User user) {
        try {
        	int generateId=idGeneratorService.generateId(user.getRole());
        	user.setId(String.valueOf(generateId));
        	if(user.getRole().equals("STUDENT")) {
        		Student student=new Student(String.valueOf(generateId),user.getUsername(),user.getPassword(),user.getEmail());
        		studentService.createStudent(student);
        	}
        	if(user.getRole().equals("ADMIN")) {
        		Admin admin =new Admin(String.valueOf(generateId),user.getUsername(),user.getEmail(),user.getPassword());
        		adminService.saveAdmin(admin);
        	}
        	if(user.getRole().equals("INSTRUCTOR")) {
        		Instructor instructor=new Instructor(String.valueOf(generateId),user.getUsername(),user.getEmail(),user.getPassword());
        		instructorService.saveInstructor(instructor);
        	}
            User savedUser = userService.registerUser(user);
            return ResponseEntity.ok(Map.of("message", "User registered successfully", "userId", savedUser.getId()));
        } catch (Exception e) {
            logger.error("Signup failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        logger.info("Login attempt: {}", loginRequest.getUsername());

        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );

            logger.info("✅ Authentication successful: {}", loginRequest.getUsername());

            UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());
            
            String token = jwtUtil.generateToken(userDetails);
            Optional<User> userOpt = userService.getUserByName(loginRequest.getUsername());
            if (userOpt.isPresent()) {
                User user=userOpt.get();
                String role=user.getRole();
                return ResponseEntity.ok(Map.of("token", token, "role", role));  // ✅ Return role
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "User not found"));
            }
        } catch (BadCredentialsException e) {
            logger.warn("❌ Authentication failed for {}: Invalid credentials", loginRequest.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid username or password"));
        } catch (Exception e) {
            logger.error("❌ Authentication error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "An unexpected error occurred"));
        }
    }
}
