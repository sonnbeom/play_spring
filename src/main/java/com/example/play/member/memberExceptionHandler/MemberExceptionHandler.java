package com.example.play.member.memberExceptionHandler;

import com.example.play.member.exception.MemberNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class MemberExceptionHandler {
    @ExceptionHandler(com.example.play.member.exception.MemberNotFoundException.class)
    public ResponseEntity<String> memberNotFoundException(MemberNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
