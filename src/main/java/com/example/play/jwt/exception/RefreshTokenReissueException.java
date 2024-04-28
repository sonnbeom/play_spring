package com.example.play.jwt.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class RefreshTokenReissueException extends RuntimeException{
    @Getter
    private final HttpStatus status;
    public RefreshTokenReissueException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
