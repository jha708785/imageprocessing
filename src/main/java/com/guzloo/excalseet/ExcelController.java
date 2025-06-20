package com.guzloo.excalseet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class ExcelController {

    @Autowired
    private StudentService service;
    @Autowired
    private ExcelService excelService;

    @PostMapping("/import-excal")
    public ResponseEntity<?> importExcalFile(@RequestParam MultipartFile file) {
        try {

            List<Student> students = excelService.importExcel(file);
            Boolean b = service.saveStudent(students);
            if (b) {
                return new ResponseEntity<>("upload success", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("upload failed ! please try again", HttpStatus.OK);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/generate-file")
    public void generateExcalFile(HttpServletResponse response) {
        try {
            List<Student> listStudent = service.getAllStudent();
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("content-Disposition", "attachment;filename=student_dtls.xlsx");
            excelService.generateExcel(listStudent, response);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
