package com.example.play.jwt.exception;

import org.springframework.http.HttpStatus;

public class RefreshTokenReissueException extends RuntimeException{

    public RefreshTokenReissueException(String message, HttpStatus httpStatus) {
    }
}
