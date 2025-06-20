package com.guzloo.excalseet;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ExcelService {

    public List<Student> importExcel(MultipartFile file) throws Exception;

    public void generateExcel(List<Student> products, HttpServletResponse response) throws IOException;

    void generateExcelToDisk(List<Student> students) throws IOException;
}
