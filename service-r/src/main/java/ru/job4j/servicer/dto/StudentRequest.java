package ru.job4j.servicer.dto;

public class StudentRequest {

    private String requestId;
    private String studentId;

    public StudentRequest() {
    }

    public StudentRequest(String requestId, String studentId) {
        this.requestId = requestId;
        this.studentId = studentId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
}
