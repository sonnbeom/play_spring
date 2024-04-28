package com.example.play.post.exceptionHandler;

import com.example.play.post.exception.PostDeleteException;
import com.example.play.post.exception.PostNotFoundException;
import com.example.play.post.exception.PostUpdateException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class PostExceptionHandler {
    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<String> handlePostNotFoundException(PostNotFoundException ex){
        return new ResponseEntity<>(ex.getMessage(), ex.getStatus());
    }
    @ExceptionHandler(PostDeleteException.class)
    public ResponseEntity<String> handlePostDeleteException(PostDeleteException ex){
        return new ResponseEntity<>(ex.getMessage(), ex.getStatus());
    }
    @ExceptionHandler(PostUpdateException.class)
    public ResponseEntity<String> handlePostUpdateException(PostUpdateException ex){
        return new ResponseEntity<>(ex.getMessage(), ex.getStatus());
    }
}
