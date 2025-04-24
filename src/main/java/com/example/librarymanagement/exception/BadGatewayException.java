package com.example.librarymanagement.exception;

public class BadGatewayException extends RuntimeException {

    private Object[] params;

    public BadGatewayException(String message) {
        super(message);
    }

    public BadGatewayException(String message, Object... params){
      super(message);
      this.params = params;
    }
}
