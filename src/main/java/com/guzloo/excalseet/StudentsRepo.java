package com.guzloo.excalseet;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentsRepo extends JpaRepository<Student,Long> {

    List<Student> findByClassesAndDepartment(String classes, String department);
}
