package com.example.cryptocurrency.model.exception;

public class InputInvalidException extends RuntimeException {
    public InputInvalidException() {
        super();
    }

    public InputInvalidException(String message) {
        super(message);
    }

    public InputInvalidException(String message, Throwable cause) {
        super(message, cause);
    }

    public InputInvalidException(Throwable cause) {
        super(cause);
    }
}
