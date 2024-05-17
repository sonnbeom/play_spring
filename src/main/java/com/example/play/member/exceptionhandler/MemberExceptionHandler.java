package com.example.play.member.exceptionhandler;

import com.example.play.member.exception.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class MemberExceptionHandler {
    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<String> handleMemberNotFoundException(MemberNotFoundException ex){
        return new ResponseEntity<>(ex.getMessage(), ex.getStatus());
    }
    @ExceptionHandler(DuplicateMemberEmailException.class)
    public ResponseEntity<String> handleDuplicateMemberEmailException(DuplicateMemberEmailException ex){
        return new ResponseEntity<>(ex.getMessage(), ex.getStatus());
    }
    @ExceptionHandler(DuplicateMemberNicknameException.class)
    public ResponseEntity<String> handleDuplicateMemberNicknameException(DuplicateMemberNicknameException ex){
        return new ResponseEntity<>(ex.getMessage(), ex.getStatus());
    }
    @ExceptionHandler(MemberDeleteAuthorityException.class)
    public ResponseEntity<String> handleMemberDeleteAuthorityException(MemberDeleteAuthorityException ex){
        return new ResponseEntity<>(ex.getMessage(), ex.getStatus());
    }
    @ExceptionHandler(MemberGetAuthorityException.class)
    public ResponseEntity<String> handleMemberGetAuthorityException(MemberGetAuthorityException ex){
        return new ResponseEntity<>(ex.getMessage(), ex.getStatus());
    }
}
