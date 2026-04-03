package ru.job4j.servicer.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

public class StudentResponse {

    private String requestId;
    private String studentId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private StudentData student;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String photo;
    private boolean success;
    private String errorMessage;

    public StudentResponse() {
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

    public StudentData getStudent() {
        return student;
    }

    public void setStudent(StudentData student) {
        this.student = student;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
