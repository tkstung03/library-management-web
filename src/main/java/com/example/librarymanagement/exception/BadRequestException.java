package com.example.librarymanagement.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BadRequestException extends RuntimeException {

    private Object[] params;

    public BadRequestException(String message, Object... args) {
        super(message);
        this.params = args;
    }

    public BadRequestException(String message) {
        super(message);
    }
}
