package com.example.librarymanagement.exception;

public class ForbiddenException extends RuntimeException {

    private Object[] params;

    public ForbiddenException(String message) {
        super(message);
    }

    public ForbiddenException(String message, Object... params) {
        super(message);
        this.params = params;
    }
}
