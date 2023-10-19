package com.example.cryptocurrency.model.exception;

public class HTTPResponseException extends RuntimeException {
    public HTTPResponseException() {
        super();
    }

    public HTTPResponseException(String message) {
        super(message);
    }

    public HTTPResponseException(String message, Throwable cause) {
        super(message, cause);
    }

    public HTTPResponseException(Throwable cause) {
        super(cause);
    }
}