package com.example.play.auth.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class PasswordNotMatchException extends RuntimeException{
    @Getter
    private final HttpStatus status;
    public PasswordNotMatchException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
