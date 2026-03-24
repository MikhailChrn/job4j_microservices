package ru.job4j.services.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.job4j.services.model.Student;
import ru.job4j.services.service.StudentService;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
    }

    @GetMapping
    public List<Student> findAll() {
        return service.findAll();
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<Student> findByStudentId(@PathVariable String studentId) {
        return service.findByStudentId(studentId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{studentId}/photo")
    public ResponseEntity<byte[]> getPhoto(@PathVariable String studentId) {
        return service.getStudentPhoto(studentId)
                .map(data -> ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(data))
                .orElse(ResponseEntity.notFound().build());
    }
}
