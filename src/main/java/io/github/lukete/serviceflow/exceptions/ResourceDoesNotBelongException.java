package io.github.lukete.serviceflow.exceptions;

public class ResourceDoesNotBelongException extends RuntimeException {
    public ResourceDoesNotBelongException(String message) {
        super(message);
    }

    public ResourceDoesNotBelongException(String message, Throwable cause) {
        super(message, cause);
    }
}
