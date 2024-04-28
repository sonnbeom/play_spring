package com.example.play.post.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class PostUpdateException extends RuntimeException{
    @Getter
    private final HttpStatus status;
    public PostUpdateException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

}

