package com.example.play.like.post.exceptionHandler;

import com.example.play.like.post.exception.PostLikeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class PostLikeExceptionHandler {
    @ExceptionHandler(PostLikeException.class)
    public ResponseEntity<String> handlePostLikeCountException(PostLikeException ex) {
        return new ResponseEntity<>(ex.getMessage(), ex.getStatus());
    }
}

