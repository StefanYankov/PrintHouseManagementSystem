package org.PrintHouse.exceptions;

public class InvalidPageCountException extends RuntimeException {
    public InvalidPageCountException(String message) {
        super(message);
    }
}