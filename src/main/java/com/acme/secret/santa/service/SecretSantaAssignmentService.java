package com.acme.secret.santa.service;


import com.acme.secret.santa.exception.AssignmentException;
import com.acme.secret.santa.model.Employee;
import com.acme.secret.santa.model.SecretSantaAssignment;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SecretSantaAssignmentService {
    public List<SecretSantaAssignment> assignSecretSanta(List<Employee> employees, List<SecretSantaAssignment> prevAssignment) {
        if (employees == null || employees.isEmpty()) {
            throw new AssignmentException("Employee list is empty. Cannot assign Secret Santa.");
        }
        if (employees.size() == 1) {
            throw new AssignmentException("At least two employees are required for Secret Santa.");
        }
        List<Employee> shuffled = new ArrayList<>(employees);
        //shuffle employees
        Collections.shuffle(shuffled);
        Map<String, Employee> assignment = new HashMap<>();
        for (Employee santa : employees) {
            List<Employee> eligibleChildren = new ArrayList<>();
            for (Employee child : shuffled) {
                //employee should not assign themselves
                //previous year secret child should not repeat for the same parent
                if (!santa.equals(child) && prevAssignment.stream().noneMatch(x -> x.employeeEmail().equals(santa.employeeEmail()) && x.secretChildEmail().equals(child.employeeEmail()))) {
                    eligibleChildren.add(child);
                }
            }
            if (eligibleChildren.isEmpty()) {
                throw new AssignmentException("Failed to assign Secret Santa due to constraints. Please check input.");
            }
            Employee assignChild = eligibleChildren.get(new Random().nextInt(eligibleChildren.size()));
            assignment.put(santa.employeeEmail(), assignChild);
            //remove assigned child
            shuffled.remove(assignChild);
        }
        List<SecretSantaAssignment> secretSantaAssignments = new ArrayList<>();
        for (Employee santa : employees) {
            Employee assignChild = assignment.get(santa.employeeEmail());
            secretSantaAssignments.add(new SecretSantaAssignment(santa.employeeName(), santa.employeeEmail(), assignChild.employeeName(), assignChild.employeeEmail()));
        }
        return secretSantaAssignments;
    }
}
