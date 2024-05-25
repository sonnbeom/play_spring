package com.example.play.comment.exception;

import org.springframework.http.HttpStatus;

public class CommentNotFoundException extends RuntimeException{
    private final HttpStatus httpStatus;

    public CommentNotFoundException(String message ,HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
