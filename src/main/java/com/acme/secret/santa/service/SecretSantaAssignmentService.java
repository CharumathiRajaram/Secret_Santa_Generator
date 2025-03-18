package com.acme.secret.santa.service;


import com.acme.secret.santa.exception.AssignmentException;
import com.acme.secret.santa.model.Employee;
import com.acme.secret.santa.model.SecretSantaAssignment;
import com.acme.secret.santa.utils.BaseLogger;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Service for assigning Secret Santa pairs.
 */
@Service
public class SecretSantaAssignmentService extends BaseLogger {
    /**
     * Assigns Secret Santa pairs to employees while ensuring constraints are met.
     *
     * @param employees      The list of employees participating in the Secret Santa event.
     * @param prevAssignment The list of previous year's Secret Santa assignments (can be empty).
     * @return A list of {@link SecretSantaAssignment} containing the assigned pairs.
     * @throws AssignmentException if constraints prevent successful assignment.
     */
    public List<SecretSantaAssignment> assignSecretSanta(List<Employee> employees, List<SecretSantaAssignment> prevAssignment) {
        logger.info("Starting Secret Santa assignment process...");
        if (employees == null || employees.isEmpty()) {
            logger.warn("Employee list is empty. Cannot assign Secret Santa.");
            throw new AssignmentException("Employee list is empty. Cannot assign Secret Santa.");
        }
        if (employees.size() == 1) {
            logger.warn("Only one employee provided. At least two employees are required.");
            throw new AssignmentException("At least two employees are required for Secret Santa.");
        }
        List<Employee> shuffled = new ArrayList<>(employees);
        //shuffle employees
        Collections.shuffle(shuffled);
        logger.debug("Shuffled employee list: {}", shuffled);
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
                logger.error("Failed to assign Secret Santa for {} due to constraints.", santa.employeeEmail());
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
        logger.debug("Successfully assigned Secret Santa to {} employees.", secretSantaAssignments.size());
        return secretSantaAssignments;
    }
}
