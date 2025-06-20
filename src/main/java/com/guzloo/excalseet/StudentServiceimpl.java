package com.guzloo.excalseet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class StudentServiceimpl implements StudentService{

    @Autowired
    private StudentsRepo studentsRepo;

    @Override
    public Boolean saveStudent(List<Student> students) {
        List<Student> saveAll = studentsRepo.saveAll(students);
        if (!CollectionUtils.isEmpty(saveAll)){
            return true;
        }
        return false;
    }

    @Override
    public List<Student> getAllStudent() {
        return studentsRepo.findAll();
    }
}
