package utilities.exceptions;

public class InvalidPrintRequestException extends RuntimeException {
    public InvalidPrintRequestException(String message) {
        super(message);
    }
}