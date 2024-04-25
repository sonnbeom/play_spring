package com.example.play.chat.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class ChatException extends RuntimeException{
    @Getter
    private final HttpStatus status;

    public ChatException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
