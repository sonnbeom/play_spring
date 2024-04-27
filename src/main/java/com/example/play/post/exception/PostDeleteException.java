package com.example.play.post.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class PostDeleteException extends RuntimeException{
    @Getter
    private final HttpStatus status;

    public PostDeleteException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
