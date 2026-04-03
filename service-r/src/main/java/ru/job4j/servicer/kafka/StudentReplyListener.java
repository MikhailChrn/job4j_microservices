package ru.job4j.servicer.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.job4j.servicer.dto.StudentResponse;
import ru.job4j.servicer.service.StudentGateway;
import ru.job4j.servicer.service.XmlJsonConverter;

@Component
public class StudentReplyListener {

    private static final Logger log = LoggerFactory.getLogger(StudentReplyListener.class);

    private final StudentGateway gateway;
    private final XmlJsonConverter converter;
    private final ObjectMapper jsonMapper;

    public StudentReplyListener(StudentGateway gateway, XmlJsonConverter converter, ObjectMapper jsonMapper) {
        this.gateway = gateway;
        this.converter = converter;
        this.jsonMapper = jsonMapper;
    }

    @KafkaListener(topics = "student-reply", groupId = "service-r-group")
    public void onReply(String xml) {
        try {
            String json = converter.xmlToJson(xml);
            log.info("Received XML, converted to JSON");
            StudentResponse response = jsonMapper.readValue(json, StudentResponse.class);
            gateway.complete(response);
        } catch (Exception e) {
            log.error("Failed to parse XML reply from Kafka", e);
        }
    }
}
