package com.acme.secret.santa.utils;

import com.acme.secret.santa.model.Employee;
import com.acme.secret.santa.model.SecretSantaAssignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CSVProcessor {

    public static <T> List<T> parseFile(MultipartFile file, Class<T> type) throws IOException {
        String fileName = file.getOriginalFilename();
        if (fileName.isBlank()) throw new IllegalArgumentException("File is empty");
        if (fileName.endsWith(".csv")) {
            return parseCSV(file, type);
        } else if (fileName.endsWith(".xlsx")) {
            return parseExcel(file, type);
        } else {
            throw new IllegalArgumentException("Unsupported File Format.Please Upload CSV or Excel file");
        }
    }

    private static <T> List<T> parseCSV(MultipartFile file, Class<T> type) throws IOException {
        List<T> data = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            boolean isHeader = true;
            while ((line = bufferedReader.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }
                if (!line.trim().isEmpty()) {
                    String[] fields = line.split(",");
                    if (type == Employee.class) {
                        data.add(type.cast(new Employee(fields[0], fields[1])));
                    } else if (type == SecretSantaAssignment.class) {
                        data.add(type.cast(new SecretSantaAssignment(fields[0], fields[1], fields[2], fields[3])));
                    }
                }
            }
        }
        return data;
    }

    private static <T> List<T> parseExcel(MultipartFile file, Class<T> type) throws IOException {
        List<T> data = new ArrayList<>();
        try (InputStream is = file.getInputStream();
             Workbook workBook = WorkbookFactory.create(is)) {

            Sheet sheet = workBook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;
                if (type == Employee.class) {
                    data.add(type.cast(new Employee(row.getCell(0).getStringCellValue(), row.getCell(1).getStringCellValue())));
                } else if (type == SecretSantaAssignment.class) {
                    data.add(type.cast(new SecretSantaAssignment(row.getCell(0).getStringCellValue(),
                            row.getCell(1).getStringCellValue(),
                            row.getCell(2).getStringCellValue(),
                            row.getCell(3).getStringCellValue())));
                }
            }
            return data;
        }
    }

}
