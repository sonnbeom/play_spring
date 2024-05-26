package com.example.play.comment.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class CommentUpdateAuthorizationException extends RuntimeException{
    @Getter
    private final HttpStatus status;

    public CommentUpdateAuthorizationException(String message ,HttpStatus status) {
        super(message);
        this.status = status;
    }
}
