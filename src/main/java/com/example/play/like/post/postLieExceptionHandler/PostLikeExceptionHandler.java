package com.example.play.like.post.postLieExceptionHandler;

import com.example.play.like.post.exception.PostLikeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class PostLikeExceptionHandler {
    @ExceptionHandler(PostLikeException.class)
    public ResponseEntity<String> PostLikeCountException(PostLikeException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());

    }
}

