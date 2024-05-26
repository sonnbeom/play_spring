package com.example.play.comment.exceptionhandler;

import com.example.play.comment.exception.CommentUpdateAuthorizationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CommentExceptionHandler {
    @ExceptionHandler(CommentUpdateAuthorizationException.class)
    public ResponseEntity<String> handleCommentUpdateAuthorizationException(CommentUpdateAuthorizationException ex){
        return new ResponseEntity<>(ex.getMessage(), ex.getStatus());
    }
}
