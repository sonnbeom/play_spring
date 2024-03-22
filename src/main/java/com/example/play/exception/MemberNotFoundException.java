package com.example.play.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MemberNotFoundException extends RuntimeException{

    public MemberNotFoundException(String message) {
        log.info(message);
    }
}
