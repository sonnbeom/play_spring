package com.example.play.image.exception;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class FileExtensionException extends RuntimeException{
    public FileExtensionException() {
    }

    public FileExtensionException(String message) {
        super(message);
        log.info(message);
    }
}
