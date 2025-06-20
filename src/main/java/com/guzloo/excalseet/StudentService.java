package com.guzloo.excalseet;

import org.springframework.stereotype.Service;

import java.util.List;


public interface StudentService {

    public Boolean saveStudent(List<Student> productDtls);

    public List<Student> getAllStudent();
}
