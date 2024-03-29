package com.example.play.image.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MinioUploadException extends RuntimeException{
    public MinioUploadException() {
    }

    public MinioUploadException(String message) {
        super(message);
        log.info(message);
    }
}
