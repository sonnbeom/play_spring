package com.example.play.member.exception;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor
@Slf4j
public class MemberNotFoundException extends RuntimeException{
    public MemberNotFoundException(String message) {
        super(message);
        log.info(message);
    }

    public MemberNotFoundException(String message, String email) {
        super(String.format(message, email));
    }
}
