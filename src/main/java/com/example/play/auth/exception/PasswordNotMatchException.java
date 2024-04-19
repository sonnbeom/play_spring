package com.example.play.auth.exception;

public class PasswordNotMatchException extends RuntimeException{
    public PasswordNotMatchException() {
    }

    public PasswordNotMatchException(String message) {
        super(message);
    }

    public PasswordNotMatchException(String message, String inputPassword, String password) {
        super();
    }
}
