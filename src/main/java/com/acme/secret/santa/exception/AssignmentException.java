package com.acme.secret.santa.exception;

/**
 * Exception thrown when there is an error during Assignment.
 */
public class AssignmentException extends RuntimeException {
    /**
     * Constructs a new {@code AssignmentException} with the specified detail message.
     *
     * @param message The detail message explaining the reason for the exception.
     */
    public AssignmentException(String message) {
        super(message);
    }
}
