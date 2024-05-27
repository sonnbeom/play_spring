package com.example.play.comment.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class CommentDeleteAuthorizationException extends RuntimeException{
    @Getter
    private final HttpStatus status;

    public CommentDeleteAuthorizationException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
