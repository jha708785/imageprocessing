/*package com.guzloo.mail;

import com.guzloo.excalseet.Student;
import com.guzloo.excalseet.StudentsRepo;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class SendFileOnEmail {

   /* @Autowired
    private EmailService emailService;*/
   /* private StudentsRepo repo;

    @Scheduled(cron = "*///30 * * * * *") // or daily
  /*  public void exportAndEmailExcelFile() throws IOException {
        List<Student> students = repo.findAll();

        // Step 1: Generate Excel to disk
        String filename = "students_" + System.currentTimeMillis() + ".xlsx";
        String path = "C:\\Users\\Suman Kumar\\Downloads\\" + filename;

        generateExcelToDisk(students, path);

        // Step 2: Send email with attachment
        File file = new File(path);
        emailService.sendExcelAsAttachment(
                "admin@guzloo.com",   // To
                "Daily Student Report", // Subject
                "Please find the attached student list.", // Message
                file
        );
    }


    public void generateExcelToDisk(List<Student> students, String filePath) throws IOException {
        FileOutputStream out = null;
        XSSFWorkbook workbook = new XSSFWorkbook();

        try {
            XSSFSheet sheet = workbook.createSheet("Student_Details");

            // Header Style
            XSSFCellStyle headerStyle = workbook.createCellStyle();
            XSSFFont headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeight(12);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // Data Style
            XSSFCellStyle dataStyle = workbook.createCellStyle();
            XSSFFont dataFont = workbook.createFont();
            dataFont.setFontHeight(10);
            dataStyle.setFont(dataFont);

            // Header Row
            String[] headers = {"Name", "Email", "Phone", "Gender", "Age", "Class", "Parent Contact", "Department"};
            XSSFRow headerRow = sheet.createRow(0);
            createCellS(sheet, headerRow, headers, headerStyle);

            // 👥 Student Rows
            int rowCount = 1;
            for (Student s : students) {
                XSSFRow row = sheet.createRow(rowCount++);
                Object[] data = {
                        s.getName(), s.getEmail(), s.getPhone(), s.getGender(),
                        s.getAge(), s.getClasses(), s.getParentcontact(), s.getDepartment()
                };
                createCellS(sheet, row, data, dataStyle);
            }

            // Write file to disk
            out = new FileOutputStream(filePath);
            workbook.write(out);
            System.out.println("Excel file saved to: " + filePath);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) out.close();
            workbook.close();
        }
    }



}*/
