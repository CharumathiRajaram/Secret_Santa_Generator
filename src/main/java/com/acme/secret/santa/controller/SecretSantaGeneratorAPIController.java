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

/**
 * REST controller to handle Secret Santa assignment generation.
 * It processes the incoming employee CSV file, handles previous assignments if provided,
 * generates the Secret Santa assignments, and returns a CSV file with the generated assignments.
 */
@RestController
@RequestMapping("/secret_santa")
public class SecretSantaGeneratorAPIController extends BaseLogger {

    private final CSVService csvService;
    private final SecretSantaAssignmentService secretSantaAssignmentService;

    /**
     * Constructor to initialize the SecretSantaGeneratorAPIController.
     *
     * @param csvService                   the CSV parsing service
     * @param secretSantaAssignmentService the Secret Santa assignment service
     */
    private SecretSantaGeneratorAPIController(CSVService csvService, SecretSantaAssignmentService secretSantaAssignmentService) {
        this.csvService = csvService;
        this.secretSantaAssignmentService = secretSantaAssignmentService;
    }

    /**
     * Endpoint to handle the generation of Secret Santa assignments.
     *
     * @param employeeFile the CSV file containing employee details
     * @param prevFile     an optional CSV file containing previous Secret Santa assignments
     * @return ResponseEntity containing the generated assignments in CSV format
     */
    @PostMapping("/assign")
    public ResponseEntity<byte[]> assignSecretSanta(@RequestParam("employees") MultipartFile employeeFile,
                                                    @RequestParam(value = "previous", required = false) MultipartFile prevFile) {
        try {
            logger.info("Received request to assign Secret Santa");

            // Validate employee file
            if (employeeFile == null || employeeFile.isEmpty()) {
                logger.warn("Employee file is missing or empty");
                throw new IllegalArgumentException("Employee file is required and cannot be empty.");
            }

            // Parse the employee CSV file
            logger.info("Parsing employee CSV file");
            List<Employee> employees = csvService.parseEmployeeCsv(employeeFile);
            logger.info("Parsed {} employees from the file", employees.size());

            List<SecretSantaAssignment> prevAssignments = List.of();
            if (prevFile != null && !prevFile.isEmpty()) {
                // Parse previous Secret Santa assignments if the file is provided
                logger.info("Parsing previous Secret Santa assignments file");
                prevAssignments = csvService.parsePrevAssignmentCSV(prevFile);
                logger.info("Parsed {} previous assignments", prevAssignments.size());
            }

            // Generate new Secret Santa assignments
            logger.info("Generating Secret Santa assignments");
            List<SecretSantaAssignment> assignments = secretSantaAssignmentService.assignSecretSanta(employees, prevAssignments);
            logger.info("Successfully assigned {}", assignments.size());

            // Convert assignments to CSV format
            StringJoiner csvContent = new StringJoiner("\n");
            csvContent.add("Employee_Name,Employee_EmailID,Secret_Child_Name,Secret_Child_EmailID");
            for (SecretSantaAssignment assign : assignments) {
                csvContent.add(assign.employeeName() + "," + assign.employeeEmail() + "," + assign.secretChildName() + "," + assign.secretChildEmail());
            }

            // Return the generated assignments as a CSV file
            byte[] csvBytes = csvContent.toString().getBytes(StandardCharsets.UTF_8);
            logger.info("Returning Secret Santa assignments CSV file");
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=secret_santa.csv")
                    .contentType(MediaType.TEXT_PLAIN).body(csvBytes);

        } catch (FileProcessingException e) {
            // Handle file processing errors
            logger.error("File Processing Error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(("File Processing Error: " + e.getMessage()).getBytes(StandardCharsets.UTF_8));
        } catch (IllegalArgumentException e) {
            // Handle invalid request errors
            logger.warn("Invalid request: {}", e.getMessage());
            return ResponseEntity.badRequest().body(("Invalid Request: " + e.getMessage()).getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            // Handle unexpected errors
            logger.error("Unexpected Error: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(("Unexpected Error: " + e.getMessage()).getBytes(StandardCharsets.UTF_8));
        }
    }

}
