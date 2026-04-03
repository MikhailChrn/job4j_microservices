package ru.job4j.servicer.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.job4j.servicer.dto.StudentResponse;
import ru.job4j.servicer.service.StudentGateway;

@Component
public class StudentReplyListener {

    private final StudentGateway gateway;

    public StudentReplyListener(StudentGateway gateway) {
        this.gateway = gateway;
    }

    @KafkaListener(topics = "student-reply", groupId = "service-r-group")
    public void onReply(StudentResponse response) {
        gateway.complete(response);
    }
}
