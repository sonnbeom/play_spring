package com.example.play.image.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class FileExtensionException extends RuntimeException{
    @Getter
    private final HttpStatus status;
    public FileExtensionException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
