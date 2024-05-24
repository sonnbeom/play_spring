package com.example.play.chatroom.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class ChatRoomException extends RuntimeException{
    @Getter
    private final HttpStatus status;

    public ChatRoomException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
