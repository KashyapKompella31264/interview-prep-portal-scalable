package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.demo.model.Student;
import com.example.demo.repository.StudentRepository;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private StudentService studentService;

    @Test
    void testFindStudentById() {
        Student s = new Student();
        s.setId("101");
        s.setName("Kashyap");

        when(studentRepository.findById("101"))
                .thenReturn(Optional.of(s));

        Optional<Student> result = studentService.findStudentById("101");

        assertTrue(result.isPresent());
        assertEquals("Kashyap", result.get().getName());
        verify(studentRepository, times(1)).findById("101");
    }

    @Test
    void testFindStudentByEmail() {
        Student s = new Student();
        s.setEmail("test@gmail.com");

        when(studentRepository.findByEmail("test@gmail.com"))
                .thenReturn(Optional.of(s));

        Optional<Student> result = studentService.findStudentByEmail("test@gmail.com");

        assertTrue(result.isPresent());
        verify(studentRepository, times(1)).findByEmail("test@gmail.com");
    }

    @Test
    void testCreateStudent() {
        Student s = new Student();
        s.setName("Ravi");

        when(studentRepository.save(s)).thenReturn(s);

        Student saved = studentService.createStudent(s);

        assertEquals("Ravi", saved.getName());
        verify(studentRepository).save(s);
    }

    @Test
    void testGetAllStudents() {
        when(studentRepository.findAll())
                .thenReturn(Arrays.asList(new Student(), new Student()));

        List<Student> list = studentService.getAllStudents();

        assertEquals(2, list.size());
        verify(studentRepository).findAll();
    }

    @Test
    void testDeleteStudent() {
        String result = studentService.deletestudent("101");

        verify(studentRepository).deleteById("101");
        assertEquals("deleted Successfully", result);
    }

    @Test
    void testUpdateStudentWithPassword() {
        Student existing = new Student();
        existing.setId("101");

        Student updated = new Student();
        updated.setName("NewName");
        updated.setPassword("plain123");

        when(studentRepository.findById("101"))
                .thenReturn(Optional.of(existing));
        when(passwordEncoder.encode("plain123"))
                .thenReturn("encoded123");
        when(studentRepository.save(existing))
                .thenReturn(existing);

        Student result = studentService.updateStudent("101", updated);

        assertEquals("NewName", result.getName());
        assertEquals("encoded123", result.getPassword());
        verify(passwordEncoder).encode("plain123");
    }

    @Test
    void testUpdateStudentWithoutPassword() {
        Student existing = new Student();
        existing.setId("101");

        Student updated = new Student();
        updated.setName("NoPass");

        when(studentRepository.findById("101"))
                .thenReturn(Optional.of(existing));
        when(studentRepository.save(existing))
                .thenReturn(existing);

        Student result = studentService.updateStudent("101", updated);

        assertEquals("NoPass", result.getName());
        verify(passwordEncoder, never()).encode(any());
    }

    @Test
    void testUpdateStudentNotFound() {
        when(studentRepository.findById("404"))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> studentService.updateStudent("404", new Student()));
    }
}
