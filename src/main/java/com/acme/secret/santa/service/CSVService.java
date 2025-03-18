package com.acme.secret.santa.service;

import com.acme.secret.santa.model.Employee;
import com.acme.secret.santa.model.SecretSantaAssignment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class CSVService {
    public List<Employee> parseEmployeeCsv(MultipartFile file) throws IOException{
        return List.of(new Employee("", ""));//todo write helper method;
    }

    public List<SecretSantaAssignment> parsePrevAssignmentCSV(MultipartFile file)  throws IOException{
        return List.of(new SecretSantaAssignment("", "","",""));//todo write helper method;
    }
}
