package ru.job4j.services.service;

import org.springframework.stereotype.Service;
import ru.job4j.services.model.Student;
import ru.job4j.services.repository.StudentRepository;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository repository;

    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }

    public List<Student> findAll() {
        return repository.findAll();
    }

    public Optional<Student> findByStudentId(String studentId) {
        return repository.findById(studentId);
    }
}
