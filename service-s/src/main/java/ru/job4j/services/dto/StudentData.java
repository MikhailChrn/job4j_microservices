package ru.job4j.services.dto;

import ru.job4j.services.model.Student;

public class StudentData {

    private String studentId;
    private String firstName;
    private String lastName;
    private String middleName;
    private String faculty;
    private String photoUrl;

    public StudentData() {
    }

    public static StudentData from(Student student) {
        StudentData data = new StudentData();
        data.setStudentId(student.getStudentId());
        data.setFirstName(student.getFirstName());
        data.setLastName(student.getLastName());
        data.setMiddleName(student.getMiddleName());
        data.setFaculty(student.getFaculty());
        data.setPhotoUrl(student.getPhotoUrl());
        return data;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
