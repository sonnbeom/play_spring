package com.example.play.post.postExceptionHandler;

import com.example.play.post.exception.PostNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class PostExceptionHandler {
    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<String> postNotFoundException(PostNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
