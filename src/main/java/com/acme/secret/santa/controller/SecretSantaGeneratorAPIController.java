package com.acme.secret.santa.controller;

import com.acme.secret.santa.model.Employee;
import com.acme.secret.santa.model.SecretSantaAssignment;
import com.acme.secret.santa.service.CSVService;
import com.acme.secret.santa.service.SecretSantaAssignmentService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.StringJoiner;

@RestController
@RequestMapping("/secret_santa")
public class SecretSantaGeneratorAPIController {
    private final CSVService csvService;
    private final SecretSantaAssignmentService secretSantaAssignmentService;

    private SecretSantaGeneratorAPIController(CSVService csvService, SecretSantaAssignmentService secretSantaAssignmentService) {
        this.csvService = csvService;
        this.secretSantaAssignmentService = secretSantaAssignmentService;
    }

    @PostMapping("/assign")
    public ResponseEntity<byte[]> assignSecretSanta(@RequestParam("employees") MultipartFile employeeFile, @RequestParam(value = "previous", required = false) MultipartFile prevFile) {
        try {
            List<Employee> employees = csvService.parseEmployeeCsv(employeeFile);
            List<SecretSantaAssignment> prevAssignments = prevFile != null ? csvService.parsePrevAssignmentCSV(prevFile) : List.of();
            List<SecretSantaAssignment> assignments = secretSantaAssignmentService.assignSecretSanta(employees, prevAssignments);
            StringJoiner csvContent = new StringJoiner("\n");
            csvContent.add("Employee_Name,Employee_EmailID,Secret_Child_Name,Secret_Child_EmailID");
            for (SecretSantaAssignment assign : assignments) {
                csvContent.add(assign.employeeName() + "," + assign.employeeEmail() + "," + assign.secretChildName() + "," + assign.secretChildEmail());
            }
            byte[] csvBytes=csvContent.toString().getBytes(StandardCharsets.UTF_8);
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=secret_santa.csv").contentType(MediaType.TEXT_PLAIN).body(csvBytes);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(("Error:" + e.getMessage()).getBytes(StandardCharsets.UTF_8));
        }
    }

}
