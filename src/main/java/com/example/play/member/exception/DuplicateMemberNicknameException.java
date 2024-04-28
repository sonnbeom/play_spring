package com.example.play.member.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class DuplicateMemberNicknameException extends RuntimeException{
    @Getter
    private final HttpStatus status;
    public DuplicateMemberNicknameException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
