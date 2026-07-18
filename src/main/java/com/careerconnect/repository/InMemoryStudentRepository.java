package com.careerconnect.repository;

import com.careerconnect.model.Student;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryStudentRepository implements StudentRepository {

    private final Map<String, Student> storage = new ConcurrentHashMap<>();

    @Override
    public Student save(Student student) {
        storage.put(student.getId(), student);
        return student;
    }

    @Override
    public Optional<Student> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public Optional<Student> findByEmail(String email) {
        return storage.values().stream()
                .filter(s -> s.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    @Override
    public List<Student> findAll() {
        return new ArrayList<>(storage.values());
    }
}
