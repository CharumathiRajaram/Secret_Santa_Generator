package com.acme.secret.santa.service;

import com.acme.secret.santa.model.Employee;
import com.acme.secret.santa.model.SecretSantaAssignment;
import com.acme.secret.santa.utils.CSVProcessor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class CSVService {
    public List<Employee> parseEmployeeCsv(MultipartFile file) throws IOException{
        return CSVProcessor.parseEmployeeCsv(file);
    }

    public List<SecretSantaAssignment> parsePrevAssignmentCSV(MultipartFile file)  throws IOException{
        return CSVProcessor.parsePrevAssignmentCSV(file);
    }
}
