package ru.job4j.servicer.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.job4j.servicer.dto.StudentRequest;
import ru.job4j.servicer.dto.StudentResponse;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
public class StudentGateway {

    private static final Logger log = LoggerFactory.getLogger(StudentGateway.class);
    private static final long TIMEOUT_SECONDS = 5;

    private final KafkaTemplate<String, StudentRequest> kafkaTemplate;
    private final Map<String, CompletableFuture<StudentResponse>> pending = new ConcurrentHashMap<>();

    public StudentGateway(KafkaTemplate<String, StudentRequest> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public StudentResponse requestStudent(String studentId) throws TimeoutException {
        String requestId = UUID.randomUUID().toString();
        CompletableFuture<StudentResponse> future = new CompletableFuture<>();
        pending.put(requestId, future);
        kafkaTemplate.send("student-request", requestId, new StudentRequest(requestId, studentId));
        log.debug("Sent request {} for studentId={}", requestId, studentId);
        try {
            return future.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            pending.remove(requestId);
            log.warn("Timeout waiting for studentId={}", studentId);
            throw e;
        } catch (Exception e) {
            pending.remove(requestId);
            throw new RuntimeException("Error waiting for student response", e);
        }
    }

    public void complete(StudentResponse response) {
        CompletableFuture<StudentResponse> future = pending.remove(response.getRequestId());
        if (future != null) {
            future.complete(response);
        } else {
            log.warn("No pending request for requestId={}", response.getRequestId());
        }
    }
}
