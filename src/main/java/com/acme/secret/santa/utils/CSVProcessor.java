package com.acme.secret.santa.utils;

import com.acme.secret.santa.model.Employee;
import com.acme.secret.santa.model.SecretSantaAssignment;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CSVProcessor {

    public static List<Employee> parseEmployeeCsv(MultipartFile file) throws IOException {
        List<Employee> employees = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
        CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader());
        for (CSVRecord csvRecord : csvParser) {
            employees.add(new Employee(csvRecord.get("Employee_Name"),csvRecord.get("Employee_EmailID")));
        }
        return employees;
    }
    public static List<SecretSantaAssignment> parsePrevAssignmentCSV(MultipartFile file) throws IOException{
        List<SecretSantaAssignment> prevAssignment=new ArrayList<>();
        BufferedReader reader=new BufferedReader(new InputStreamReader(file.getInputStream()));
        CSVParser csvParser=new CSVParser(reader,CSVFormat.DEFAULT.withFirstRecordAsHeader());
        for(CSVRecord csvRecord:csvParser){
            prevAssignment.add(new SecretSantaAssignment(csvRecord.get("Employee_name"),csvRecord.get("Employee_EmailID"),csvRecord.get("Secret_Child_Name"),csvRecord.get("Secret_Child_EmailID")));
        }
        return prevAssignment;
    }
}
