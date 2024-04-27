package com.example.play.like.post.exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;

public class PostLikeException extends RuntimeException{
    @Getter
    private final HttpStatus status;
    public PostLikeException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
