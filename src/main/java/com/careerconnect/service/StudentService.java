package com.careerconnect.service;

import com.careerconnect.exception.DuplicateResourceException;
import com.careerconnect.exception.ResourceNotFoundException;
import com.careerconnect.model.Student;
import com.careerconnect.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class StudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);
    private final StudentRepository studentRepository;
    private final AtomicInteger idCounter = new AtomicInteger(1);

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student createStudent(String name, String email, String programme,
                                 int graduationYear, double cgpa, int activeBacklogs,
                                 List<String> skills) {
        if (studentRepository.findByEmail(email).isPresent()) {
            throw new DuplicateResourceException("A student with email " + email + " already exists.");
        }

        String id = "STU-" + String.format("%03d", idCounter.getAndIncrement());
        Student student = new Student(id, name, email, programme, graduationYear, cgpa, activeBacklogs, skills);
        studentRepository.save(student);
        logger.info("Created student: {} ({})", id, email);
        return student;
    }

    public Student getStudentById(String studentId) {
        return studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + studentId));
    }

    public Student updateStudent(String studentId, String name, String programme,
                                 int graduationYear, double cgpa, int activeBacklogs,
                                 List<String> skills) {
        Student student = getStudentById(studentId);
        student.updateProfile(name, programme, graduationYear, cgpa, activeBacklogs, skills);
        studentRepository.save(student);
        logger.info("Updated student: {}", studentId);
        return student;
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }
}
