package com.example.play.member.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class MemberDeleteAuthorityException extends RuntimeException{
    @Getter
    private final HttpStatus status;

    public MemberDeleteAuthorityException(String message ,HttpStatus status) {
        super(message);
        this.status = status;
    }
}
