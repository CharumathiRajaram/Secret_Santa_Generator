package com.acme.secret.santa.service;

import com.acme.secret.santa.exception.FileProcessingException;
import com.acme.secret.santa.model.Employee;
import com.acme.secret.santa.model.SecretSantaAssignment;
import com.acme.secret.santa.utils.BaseLogger;
import com.acme.secret.santa.utils.CSVProcessor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class CSVService extends BaseLogger {
    public List<Employee> parseEmployeeCsv(MultipartFile file) {
        try {
            logger.debug("Parsing Employee CSV file: {}", file.getOriginalFilename());
            List<Employee> employees = CSVProcessor.parseFile(file, Employee.class);
            logger.debug("Successfully parsed {} employees", employees.size());
            return employees;
        } catch (IOException e) {
            logger.error("Failed to parse Employee CSV file: {}", file.getOriginalFilename(), e);
            throw new FileProcessingException("Failed to parse Employee CSV file.", e);
        }
    }

    public List<SecretSantaAssignment> parsePrevAssignmentCSV(MultipartFile file) {
        try {
            logger.debug("Parsing Previous Secret Santa Assignment CSV file: {}", file.getOriginalFilename());
            List<SecretSantaAssignment> assignments = CSVProcessor.parseFile(file, SecretSantaAssignment.class);
            logger.debug("Successfully parsed {} previous assignments", assignments.size());
            return assignments;
        } catch (IOException e) {
            logger.error("Failed to parse Previous Assignment CSV file: {}", file.getOriginalFilename(), e);
            throw new FileProcessingException("Failed to parse parse Previous Assignment CSV file.", e);
        }
    }
}
