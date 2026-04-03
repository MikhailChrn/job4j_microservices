package ru.job4j.services.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.job4j.services.dto.StudentRequest;
import ru.job4j.services.dto.StudentResponse;
import ru.job4j.services.service.StudentService;

@Component
public class StudentRequestListener {

    private static final Logger log = LoggerFactory.getLogger(StudentRequestListener.class);

    private final StudentService studentService;
    private final KafkaTemplate<String, StudentResponse> kafkaTemplate;

    public StudentRequestListener(StudentService studentService,
                                   KafkaTemplate<String, StudentResponse> kafkaTemplate) {
        this.studentService = studentService;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "student-request", groupId = "service-s-group")
    public void onRequest(StudentRequest request) {
        log.info("Received request {} for studentId={}", request.getRequestId(), request.getStudentId());
        StudentResponse response = studentService.buildKafkaResponse(request);
        kafkaTemplate.send("student-reply", request.getRequestId(), response);
        log.info("Sent reply for requestId={}, success={}", response.getRequestId(), response.isSuccess());
    }
}
