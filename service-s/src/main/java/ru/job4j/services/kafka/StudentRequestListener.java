package ru.job4j.services.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.job4j.services.dto.StudentRequest;
import ru.job4j.services.dto.StudentResponse;
import ru.job4j.services.service.StudentService;
import ru.job4j.services.service.XmlJsonConverter;

@Component
public class StudentRequestListener {

    private static final Logger log = LoggerFactory.getLogger(StudentRequestListener.class);

    private final StudentService studentService;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final XmlJsonConverter converter;
    private final ObjectMapper jsonMapper;

    public StudentRequestListener(StudentService studentService,
                                   KafkaTemplate<String, String> kafkaTemplate,
                                   XmlJsonConverter converter,
                                   ObjectMapper jsonMapper) {
        this.studentService = studentService;
        this.kafkaTemplate = kafkaTemplate;
        this.converter = converter;
        this.jsonMapper = jsonMapper;
    }

    @KafkaListener(topics = "student-request", groupId = "service-s-group")
    public void onRequest(StudentRequest request) {
        log.info("Received request {} for studentId={}", request.getRequestId(), request.getStudentId());
        StudentResponse response = studentService.buildKafkaResponse(request);
        try {
            String json = jsonMapper.writeValueAsString(response);
            String xml = converter.jsonToXml(json);
            log.info("Sending XML response for requestId={}, success={}", response.getRequestId(), response.isSuccess());
            kafkaTemplate.send("student-reply", request.getRequestId(), xml);
        } catch (Exception e) {
            log.error("Failed to convert response to XML for requestId={}", request.getRequestId(), e);
        }
    }
}
