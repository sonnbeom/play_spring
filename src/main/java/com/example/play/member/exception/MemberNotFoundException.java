package com.example.play.member.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;


@Slf4j
public class MemberNotFoundException extends RuntimeException{
    @Getter
    private final HttpStatus status;
    public MemberNotFoundException(String message, HttpStatus status) {
        super(message);
        this.status =status;
        log.info(message);
    }

    public MemberNotFoundException(String message, String email, HttpStatus status) {
        super(String.format(message, email));
        this.status = status;
    }
}
