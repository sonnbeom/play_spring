package com.example.play.member.exception;

import org.springframework.http.HttpStatus;

public class DuplicateMemberNicknameException extends RuntimeException{
    private final HttpStatus status;
    public DuplicateMemberNicknameException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
