package com.acme.secret.santa.exception;

/**
 * Exception thrown when an invalid file format is encountered.
 */
public class InvalidFileFormatException extends RuntimeException {
    /**
     * Constructs a new {@code InvalidFileFormatException} with the specified detail message.
     *
     * @param message The detail message explaining the reason for the exception.
     */
    public InvalidFileFormatException(String message) {
        super(message);
    }
}
