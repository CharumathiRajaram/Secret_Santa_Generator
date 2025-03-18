package com.acme.secret.santa.model;

/**
 * Represents an employee participating in the Secret Santa assignment.
 *
 * @param employeeName  The name of the employee.
 * @param employeeEmail The email address of the employee.
 */
public record Employee(String employeeName, String employeeEmail) {
}
