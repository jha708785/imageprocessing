package com.guzloo.excalseet;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExcelServiceImpl implements ExcelService {

    @Autowired
    private StudentsRepo studentsRepo;


    @Override
    public List<Student> importExcel(MultipartFile file) throws Exception {
        ArrayList<Student> stu = new ArrayList<>();
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Please import file");
        }
        if (!file.isEmpty() && !file.getOriginalFilename().endsWith(".xlsx")) {
            throw new IllegalArgumentException("Invalid  Excal file");
        }

        InputStream inputStream = file.getInputStream();
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheetAt = workbook.getSheetAt(0);

        for (int i = 1; i <= sheetAt.getPhysicalNumberOfRows(); i++) {
            XSSFRow row = sheetAt.getRow(i);
            if (row == null)
                continue;
            String name = row.getCell(0).getStringCellValue();
            String email = row.getCell(1).getStringCellValue();
            String phone = row.getCell(2).getStringCellValue();
            String gender = row.getCell(3).getStringCellValue();
            Integer age = (int) row.getCell(4).getNumericCellValue();
            String classs = row.getCell(5).getStringCellValue();
            String pcontact = row.getCell(6).getStringCellValue();
            String dept = row.getCell(7).getStringCellValue();

            // set All value for save in db
            Student student = new Student();
            student.setName(name);
            student.setEmail(email);
            student.setPhone(phone);
            student.setGender(gender);
            student.setAge(age);
            student.setClasses(classs);
            student.setParentcontact(Long.valueOf(pcontact));
            student.setDepartment(dept);

            stu.add(student);

        }
        workbook.close();


        return stu;
    }

    private String[] header = {"Name", "Email", "Phone", "Gender", "Age", "Classes", "ParentContact", "Department"};

    @Override
    public void generateExcel(List<Student> students, HttpServletResponse response) throws IOException {
        ServletOutputStream outputStream = null;
        XSSFWorkbook workbook = new XSSFWorkbook();
        try {
            XSSFSheet sheet = workbook.createSheet("Student_Details");
            XSSFCellStyle headerStyle = workbook.createCellStyle();
            XSSFFont font = workbook.createFont();
            font.setBold(true);
            font.setFontHeight(12);
            headerStyle.setFont(font);
            headerStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            XSSFCellStyle dataStyle = workbook.createCellStyle();
            XSSFFont font2 = workbook.createFont();
            font2.setBold(false);
            font2.setFontHeight(10);
            headerStyle.setFont(font);

            int rowCount = 0;

            // Header Creation
            XSSFRow headerRow = sheet.createRow(rowCount);
            createCellS(sheet, headerRow, header, headerStyle);

            //Data creation
            for (Student s : students) {
                rowCount++;
                XSSFRow row = sheet.createRow(rowCount);
                Object[] data = {s.getName(),
                        s.getEmail(), s.getPhone(), s.getGender(),
                        s.getAge(), s.getClasses(), s.getParentcontact(), s.getDepartment()};

                createCellS(sheet, row, data, dataStyle);
            }

            outputStream = response.getOutputStream();
            workbook.write(outputStream);


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (workbook != null)
                    workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void generateExcelToDisk(List<Student> students) throws IOException {
        FileOutputStream out = null;
        XSSFWorkbook workbook = new XSSFWorkbook();

        try {
            XSSFSheet sheet = workbook.createSheet("Student_Details");

            // Header style
            XSSFCellStyle headerStyle = workbook.createCellStyle();
            XSSFFont headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeight(12);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // Data style
            XSSFCellStyle dataStyle = workbook.createCellStyle();
            XSSFFont dataFont = workbook.createFont();
            dataFont.setFontHeight(10);
            dataStyle.setFont(dataFont);

            // Header row
            String[] header = {"Name", "Email", "Phone", "Gender", "Age", "Class", "Parent Contact", "Department"};
            XSSFRow headerRow = sheet.createRow(0);
            createCellS(sheet, headerRow, header, headerStyle);

            // Student data rows
            int rowCount = 1;
            for (Student s : students) {
                XSSFRow row = sheet.createRow(rowCount++);
                Object[] data = {
                        s.getName(), s.getEmail(), s.getPhone(), s.getGender(),
                        s.getAge(), s.getClasses(), s.getParentcontact(), s.getDepartment()
                };
                createCellS(sheet, row, data, dataStyle);
            }

            // File output
            String filename = "students_" + System.currentTimeMillis() + ".xlsx";
            String path = "C:\\Users\\Suman Kumar\\Downloads\\" + filename; // FIXED: added `\\`
            out = new FileOutputStream(path);
            workbook.write(out);
            System.out.println("Excel file saved to: " + path);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null)
                out.close();
            workbook.close();
        }
    }


    private void createCellS(XSSFSheet sheet, XSSFRow row, Object[] data, XSSFCellStyle style) {

        for (int i = 0; i < data.length; i++) {
            sheet.autoSizeColumn(i);
            // create cell
            XSSFCell cell = row.createCell(i);
            Object dataValue = data[i];
            if (dataValue instanceof String) {
                cell.setCellValue((String) dataValue);
            } else if (dataValue instanceof Integer) {
                cell.setCellValue((Integer) dataValue);
            } else if (dataValue instanceof Long) {
                cell.setCellValue((Long) dataValue);
            } else if (dataValue instanceof Boolean) {
                cell.setCellValue((Boolean) dataValue);

            } else {
                cell.setCellValue(cell != null ? dataValue.toString() : "");
            }
            cell.setCellStyle(style);
        }

    }


}
