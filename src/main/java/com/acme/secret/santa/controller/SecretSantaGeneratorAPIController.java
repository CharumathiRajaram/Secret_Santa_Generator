package com.acme.secret.santa.controller;

import com.acme.secret.santa.exception.FileProcessingException;
import com.acme.secret.santa.model.Employee;
import com.acme.secret.santa.model.SecretSantaAssignment;
import com.acme.secret.santa.service.CSVService;
import com.acme.secret.santa.service.SecretSantaAssignmentService;
import com.acme.secret.santa.utils.BaseLogger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.StringJoiner;

@RestController
@RequestMapping("/secret_santa")
public class SecretSantaGeneratorAPIController extends BaseLogger {
    private final CSVService csvService;
    private final SecretSantaAssignmentService secretSantaAssignmentService;

    private SecretSantaGeneratorAPIController(CSVService csvService, SecretSantaAssignmentService secretSantaAssignmentService) {
        this.csvService = csvService;
        this.secretSantaAssignmentService = secretSantaAssignmentService;
    }

    @PostMapping("/assign")
    public ResponseEntity<byte[]> assignSecretSanta(@RequestParam("employees") MultipartFile employeeFile, @RequestParam(value = "previous", required = false) MultipartFile prevFile) {
        try {
            logger.info("Received request to assign Secret Santa");
            if (employeeFile == null || employeeFile.isEmpty()) {
                logger.warn("Employee file is missing or empty");
                throw new IllegalArgumentException("Employee file is required and cannot be empty.");
            }
            // Parse CSV files
            logger.info("Parsing employee CSV file");
            List<Employee> employees = csvService.parseEmployeeCsv(employeeFile);
            logger.info("Parsed {} employees from the file", employees.size());
            List<SecretSantaAssignment> prevAssignments = List.of();
            if (prevFile != null && !prevFile.isEmpty()) {
                logger.info("Parsing previous Secret Santa assignments file");
                prevAssignments = csvService.parsePrevAssignmentCSV(prevFile);
                logger.info("Parsed {} previous assignments", prevAssignments.size());
            }            // Generate Assignments
            logger.info("Generating Secret Santa assignments");
            List<SecretSantaAssignment> assignments = secretSantaAssignmentService.assignSecretSanta(employees, prevAssignments);
            logger.info("Successfully assigned {}", assignments.size());
            // Convert to CSV format
            StringJoiner csvContent = new StringJoiner("\n");
            csvContent.add("Employee_Name,Employee_EmailID,Secret_Child_Name,Secret_Child_EmailID");
            for (SecretSantaAssignment assign : assignments) {
                csvContent.add(assign.employeeName() + "," + assign.employeeEmail() + "," + assign.secretChildName() + "," + assign.secretChildEmail());
            }
            byte[] csvBytes = csvContent.toString().getBytes(StandardCharsets.UTF_8);
            logger.info("Returning Secret Santa assignments CSV file");
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=secret_santa.csv").contentType(MediaType.TEXT_PLAIN).body(csvBytes);
        } catch (FileProcessingException e) {
            logger.error("File Processing Error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(("File Processing Error: " + e.getMessage()).getBytes(StandardCharsets.UTF_8));
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid request: {}", e.getMessage());
            return ResponseEntity.badRequest().body(("Invalid Request " + e.getMessage()).getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            logger.error("Unexpected Error: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(("Unexpected Error:" + e.getMessage()).getBytes(StandardCharsets.UTF_8));
        }
    }

}
