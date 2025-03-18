package com.acme.secret.santa.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Global exception handler to manage application-wide exceptions and provide meaningful HTTP responses.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles exceptions related to file processing.
     *
     * @param ex The {@link FileProcessingException} that was thrown.
     * @return A {@link ResponseEntity} containing a BAD_REQUEST status and an error message.
     */
    @ExceptionHandler(FileProcessingException.class)
    public ResponseEntity<String> handleFileProcessingException(FileProcessingException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File Processing Error: " + ex.getMessage());
    }

    /**
     * Handles exceptions related to Secret Santa assignment failures.
     *
     * @param ex The {@link AssignmentException} that was thrown.
     * @return A {@link ResponseEntity} containing a BAD_REQUEST status and an error message.
     */
    @ExceptionHandler(AssignmentException.class)
    public ResponseEntity<String> handleAssignmentException(AssignmentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Assignment Error: " + ex.getMessage());
    }

    /**
     * Handles exceptions when an invalid file format is encountered.
     *
     * @param ex The {@link InvalidFileFormatException} that was thrown.
     * @return A {@link ResponseEntity} containing a BAD_REQUEST status and an error message.
     */
    @ExceptionHandler(InvalidFileFormatException.class)
    public ResponseEntity<String> handleInvalidFileFormatException(InvalidFileFormatException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid File Format: " + ex.getMessage());
    }

    /**
     * Handles any unexpected exceptions that are not explicitly caught by other handlers.
     *
     * @param ex The generic {@link Exception} that was thrown.
     * @return A {@link ResponseEntity} containing a BAD_REQUEST status and an error message.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("An unexpected error occurred: " + ex.getMessage());
    }
}
