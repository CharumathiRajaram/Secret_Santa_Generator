package com.acme.secret.santa.exception;

public class InvalidFileFormatException extends RuntimeException{
    public InvalidFileFormatException(String message){
        super(message);
    }
}
