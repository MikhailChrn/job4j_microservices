package ru.job4j.services.service;

import org.springframework.stereotype.Service;
import ru.job4j.services.model.Student;
import ru.job4j.services.repository.StudentRepository;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository repository;
    private final MinioService minioService;

    public StudentService(StudentRepository repository, MinioService minioService) {
        this.repository = repository;
        this.minioService = minioService;
    }

    public List<Student> findAll() {
        return repository.findAll();
    }

    public Optional<Student> findByStudentId(String studentId) {
        return repository.findById(studentId);
    }

    public Optional<byte[]> getStudentPhoto(String studentId) {
        return repository.findById(studentId)
                .map(Student::getPhotoUrl)
                .filter(url -> url != null && !url.isBlank())
                .map(minioService::getPhoto);
    }
}
