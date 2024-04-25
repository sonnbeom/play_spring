package com.example.play.image.exception;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
public class MinioUploadException extends RuntimeException{
    @Getter
    private final HttpStatus status;

    public MinioUploadException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
