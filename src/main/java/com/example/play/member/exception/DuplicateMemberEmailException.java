package com.example.play.member.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class DuplicateMemberEmailException extends RuntimeException{
    @Getter
    private final HttpStatus status;
    public DuplicateMemberEmailException(String message,HttpStatus status) {
        super(message);
        this.status = status;
    }
}
