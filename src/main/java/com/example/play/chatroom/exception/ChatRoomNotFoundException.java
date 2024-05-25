package com.example.play.chatroom.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class ChatRoomNotFoundException extends RuntimeException{
    @Getter
    private final HttpStatus status;

    public ChatRoomNotFoundException(String message ,HttpStatus status) {
        super(message);
        this.status = status;
    }
}
