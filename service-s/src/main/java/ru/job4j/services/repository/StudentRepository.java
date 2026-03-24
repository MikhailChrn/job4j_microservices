package ru.job4j.services.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.job4j.services.model.Student;

public interface StudentRepository extends JpaRepository<Student, String> {
}
