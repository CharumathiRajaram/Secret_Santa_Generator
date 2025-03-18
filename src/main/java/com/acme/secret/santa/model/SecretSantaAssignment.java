package com.acme.secret.santa.model;

/**
 * Represents a Secret Santa assignment where an employee (Santa) is assigned a secret child.
 *
 * @param employeeName     The name of the employee (Santa).
 * @param employeeEmail    The email of the employee (Santa).
 * @param secretChildName  The name of the assigned secret child.
 * @param secretChildEmail The email of the assigned secret child.
 */
public record SecretSantaAssignment(String employeeName, String employeeEmail,
                                    String secretChildName, String secretChildEmail) {
}
