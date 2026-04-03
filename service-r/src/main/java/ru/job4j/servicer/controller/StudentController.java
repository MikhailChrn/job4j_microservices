package ru.job4j.servicer.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.job4j.servicer.dto.StudentResponse;
import ru.job4j.servicer.service.StudentGateway;

import java.util.Map;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private static final Logger log = LoggerFactory.getLogger(StudentController.class);

    private final StudentGateway gateway;

    public StudentController(StudentGateway gateway) {
        this.gateway = gateway;
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<?> getStudent(@PathVariable String studentId) {
        try {
            StudentResponse response = gateway.requestStudent(studentId);
            if (!response.isSuccess()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", response.getErrorMessage()));
            }
            return ResponseEntity.ok(response);
        } catch (TimeoutException e) {
            log.warn("Timeout getting student {}", studentId);
            return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT)
                    .body(Map.of("error", "Request timed out"));
        }
    }
}
