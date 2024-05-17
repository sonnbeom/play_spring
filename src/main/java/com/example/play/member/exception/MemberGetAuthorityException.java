package com.example.play.member.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class MemberGetAuthorityException extends RuntimeException{
    @Getter
    private final HttpStatus status;

    public MemberGetAuthorityException(String message ,HttpStatus status) {
        super(message);
        this.status = status;
    }
}
