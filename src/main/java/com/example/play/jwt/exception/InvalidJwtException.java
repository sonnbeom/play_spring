package com.example.play.jwt.exception;

public class InvalidJwtException extends RuntimeException{

    public InvalidJwtException(String message, Throwable cause) {
        super(message, cause);
    }
}
