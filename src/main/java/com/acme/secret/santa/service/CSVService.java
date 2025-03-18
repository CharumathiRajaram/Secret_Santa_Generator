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

/**
 * Service for parsing CSV and Excel files related to employees and Secret Santa assignments.
 * Extends {@link BaseLogger} to enable logging across the service.
 */
@Service
public class CSVService extends BaseLogger {
    /**
     * Parses an employee CSV or Excel file and returns a list of {@link Employee} objects.
     *
     * @param file The {@link MultipartFile} containing employee data in CSV or Excel format.
     * @return A list of {@link Employee} objects extracted from the file.
     * @throws FileProcessingException if there is an issue parsing the file.
     */
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

    /**
     * Parses a previous Secret Santa assignment CSV or Excel file and returns a list of {@link SecretSantaAssignment} objects.
     *
     * @param file The {@link MultipartFile} containing previous Secret Santa assignments in CSV or Excel format.
     * @return A list of {@link SecretSantaAssignment} objects extracted from the file.
     * @throws FileProcessingException if there is an issue parsing the file.
     */
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
