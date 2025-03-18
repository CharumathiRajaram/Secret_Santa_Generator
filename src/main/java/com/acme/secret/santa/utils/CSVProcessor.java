package com.acme.secret.santa.utils;

import com.acme.secret.santa.exception.FileProcessingException;
import com.acme.secret.santa.exception.InvalidFileFormatException;
import com.acme.secret.santa.model.Employee;
import com.acme.secret.santa.model.SecretSantaAssignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CSVProcessor {
    private static final Logger logger = LoggerFactory.getLogger(CSVProcessor.class);

    /**
     * Parses the provided CSV or Excel file and converts it into a list of objects of the specified type.
     *
     * @param file the uploaded file (CSV or Excel)
     * @param type the class type (Employee or SecretSantaAssignment)
     * @param <T>  the generic type parameter
     * @return a list of parsed objects of the given type
     * @throws IOException if an error occurs while reading the file
     */
    public static <T> List<T> parseFile(MultipartFile file, Class<T> type) throws IOException {
        String fileName = file.getOriginalFilename();
        try {
            if (file.isEmpty() || fileName.isBlank()) {
                logger.error("File is empty or has no name.");
                throw new FileProcessingException("File is empty or null.");
            }
            logger.debug("Processing file: {}", fileName);
            if (fileName.endsWith(".csv")) {
                return parseCSV(file, type);
            } else if (fileName.endsWith(".xlsx")) {
                return parseExcel(file, type);
            } else {
                logger.error("Unsupported file format: {}", fileName);
                throw new FileProcessingException("Unsupported File Format.Please Upload CSV or Excel file");
            }
        } catch (IOException e) {
            logger.error("Error reading file: {}", fileName, e);
            throw new FileProcessingException("Error reading file", e);
        }
    }

    /**
     * Parses a CSV file and converts it into a list of objects of the specified type.
     *
     * @param file the uploaded CSV file
     * @param type the class type (Employee or SecretSantaAssignment)
     * @param <T>  the generic type parameter
     * @return a list of parsed objects of the given type
     * @throws IOException if an error occurs while reading the file
     */
    private static <T> List<T> parseCSV(MultipartFile file, Class<T> type) throws IOException {
        List<T> data = new ArrayList<>();
        logger.debug("Parsing CSV file...");
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
                    if ((type == Employee.class && fields.length < 2) ||
                            (type == SecretSantaAssignment.class && fields.length < 4)) {
                        logger.error("Invalid CSV format. Missing fields in line: {}", line);
                        throw new FileProcessingException("Invalid CSV format. Missing fields.");
                    }
                    try {
                        if (type == Employee.class) {
                            data.add(type.cast(new Employee(fields[0], fields[1])));
                        } else if (type == SecretSantaAssignment.class) {
                            data.add(type.cast(new SecretSantaAssignment(fields[0], fields[1], fields[2], fields[3])));
                        }
                        logger.debug("Successfully processed CSV row: {}", line);
                    } catch (Exception e) {
                        logger.error("Error parsing CSV row: {}", line, e);
                        throw new FileProcessingException("Error parsing CSV row:" + line, e);
                    }
                }
            }
        } catch (IOException e) {
            logger.error("Error reading CSV file", e);
            throw new FileProcessingException("Error reading CSV file", e);
        }
        logger.debug("Successfully parsed {} records from CSV file.", data.size());
        return data;
    }

    /**
     * Parses an Excel file and converts it into a list of objects of the specified type.
     *
     * @param file the uploaded Excel file
     * @param type the class type (Employee or SecretSantaAssignment)
     * @param <T>  the generic type parameter
     * @return a list of parsed objects of the given type
     * @throws IOException if an error occurs while reading the file
     */
    private static <T> List<T> parseExcel(MultipartFile file, Class<T> type) throws IOException {
        List<T> data = new ArrayList<>();
        logger.debug("Parsing Excel file...");
        try (InputStream is = file.getInputStream();
             Workbook workBook = WorkbookFactory.create(is)) {

            Sheet sheet = workBook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;
                try {
                    if (type == Employee.class) {
                        data.add(type.cast(new Employee(row.getCell(0).getStringCellValue(), row.getCell(1).getStringCellValue())));
                    } else if (type == SecretSantaAssignment.class) {
                        data.add(type.cast(new SecretSantaAssignment(row.getCell(0).getStringCellValue(),
                                row.getCell(1).getStringCellValue(),
                                row.getCell(2).getStringCellValue(),
                                row.getCell(3).getStringCellValue())));
                    }
                    logger.debug("Successfully processed Excel row: {}", row.getRowNum() + 1);
                } catch (Exception e) {
                    logger.error("Error processing Excel row: {}", row.getRowNum() + 1, e);
                    throw new FileProcessingException("Error processing Excel row: ", e);
                }
            }
        } catch (IOException | InvalidFileFormatException e) {
            logger.error("Error reading Excel file", e);
            throw new FileProcessingException("Error reading Excel file", e);
        }
        logger.debug("Successfully parsed {} records from Excel file.", data.size());
        return data;
    }

}
