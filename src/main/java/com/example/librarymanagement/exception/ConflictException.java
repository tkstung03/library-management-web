package com.example.librarymanagement.exception;

public class ConflictException extends RuntimeException {

    private Object[] params;

    public ConflictException(String message) {
        super(message);
    }

    public ConflictException(String message, Object... params){
        super(message);
        this.params = params;
    }
}
