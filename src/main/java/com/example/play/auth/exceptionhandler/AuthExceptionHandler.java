package com.example.play.auth.exceptionhandler;

import com.example.play.auth.exception.PasswordNotMatchException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AuthExceptionHandler {
    @ExceptionHandler(PasswordNotMatchException.class)
    public ResponseEntity<String> handlePasswordNotMatchException(PasswordNotMatchException ex){
        return new ResponseEntity<>(ex.getMessage(), ex.getStatus());
    }
}
