package com.acme.secret.santa.service;


import com.acme.secret.santa.model.Employee;
import com.acme.secret.santa.model.SecretSantaAssignment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SecretSantaAssignmentService {
    public List<SecretSantaAssignment> assignSecretSanta(List<Employee> employees,List<SecretSantaAssignment> prevAssignment){
        //todo perform logic
        return List.of(new SecretSantaAssignment("","","",""));
    }
}
