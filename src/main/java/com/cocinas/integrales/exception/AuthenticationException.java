package com.cocinas.integrales.exception;



@SuppressWarnings("serial")
public class AuthenticationException extends RuntimeException {
    private final int status;

    public AuthenticationException(String message, int status) {
        super(message);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
