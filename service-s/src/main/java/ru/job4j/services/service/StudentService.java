package ru.job4j.services.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.job4j.services.dto.StudentData;
import ru.job4j.services.dto.StudentRequest;
import ru.job4j.services.dto.StudentResponse;
import ru.job4j.services.model.Student;
import ru.job4j.services.repository.StudentRepository;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private static final Logger log = LoggerFactory.getLogger(StudentService.class);

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

    public StudentResponse buildKafkaResponse(StudentRequest request) {
        StudentResponse response = new StudentResponse();
        response.setRequestId(request.getRequestId());
        response.setStudentId(request.getStudentId());
        Optional<Student> found = repository.findById(request.getStudentId());
        if (found.isEmpty()) {
            response.setSuccess(false);
            response.setErrorMessage("Student not found: " + request.getStudentId());
            return response;
        }
        Student student = found.get();
        response.setStudent(StudentData.from(student));
        if (student.getPhotoUrl() != null && !student.getPhotoUrl().isBlank()) {
            try {
                response.setPhoto(minioService.getPhoto(student.getPhotoUrl()));
            } catch (Exception e) {
                log.warn("Could not load photo for studentId={}: {}", request.getStudentId(), e.getMessage());
            }
        }
        response.setSuccess(true);
        return response;
    }
}
