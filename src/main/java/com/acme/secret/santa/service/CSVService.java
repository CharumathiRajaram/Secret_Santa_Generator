package com.acme.secret.santa.service;

import com.acme.secret.santa.exception.FileProcessingException;
import com.acme.secret.santa.model.Employee;
import com.acme.secret.santa.model.SecretSantaAssignment;
import com.acme.secret.santa.utils.CSVProcessor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class CSVService {
    public List<Employee> parseEmployeeCsv(MultipartFile file) {
        try {
            return CSVProcessor.parseFile(file, Employee.class);
        } catch (IOException e) {
            throw new FileProcessingException("Failed to parse Employee CSV file.", e);
        }
    }

    public List<SecretSantaAssignment> parsePrevAssignmentCSV(MultipartFile file) {
        try {
            return CSVProcessor.parseFile(file, SecretSantaAssignment.class);
        } catch (IOException e) {
            throw new FileProcessingException("Failed to parse parse Previous Assignment CSV file.", e);
        }
    }
}
