package com.careerconnect.controller;

import com.careerconnect.dto.request.CreateStudentRequest;
import com.careerconnect.dto.request.UpdateStudentRequest;
import com.careerconnect.dto.response.StudentResponse;
import com.careerconnect.model.Student;
import com.careerconnect.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<StudentResponse> createStudent(@Valid @RequestBody CreateStudentRequest request) {
        Student student = studentService.createStudent(
                request.getName(), request.getEmail(), request.getProgramme(),
                request.getGraduationYear(), request.getCgpa(),
                request.getActiveBacklogs(), request.getSkills()
        );
        return new ResponseEntity<>(toResponse(student), HttpStatus.CREATED);
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<StudentResponse> getStudent(@PathVariable String studentId) {
        Student student = studentService.getStudentById(studentId);
        return ResponseEntity.ok(toResponse(student));
    }

    @PutMapping("/{studentId}")
    public ResponseEntity<StudentResponse> updateStudent(@PathVariable String studentId,
                                                          @Valid @RequestBody UpdateStudentRequest request) {
        Student student = studentService.updateStudent(
                studentId, request.getName(), request.getProgramme(),
                request.getGraduationYear(), request.getCgpa(),
                request.getActiveBacklogs(), request.getSkills()
        );
        return ResponseEntity.ok(toResponse(student));
    }

    private StudentResponse toResponse(Student s) {
        return new StudentResponse(s.getId(), s.getName(), s.getEmail(), s.getProgramme(),
                s.getGraduationYear(), s.getCgpa(), s.getActiveBacklogs(), s.getSkills());
    }
}
