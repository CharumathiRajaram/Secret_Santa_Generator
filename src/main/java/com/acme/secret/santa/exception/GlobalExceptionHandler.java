package com.acme.secret.santa.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FileProcessingException.class)
    public ResponseEntity<String> handleFileProcessingException(FileProcessingException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File Processing Error: " + ex.getMessage());
    }

    @ExceptionHandler(AssignmentException.class)
    public ResponseEntity<String> handleAssignmentException(AssignmentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Assignment Error: " + ex.getMessage());
    }
    @ExceptionHandler(InvalidFileFormatException.class)
    public ResponseEntity<String> handleInvalidFileFormatException(InvalidFileFormatException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid File Format: "+ ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("An unexpected error occurred: "+ ex.getMessage());
    }
}
