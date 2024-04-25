package com.example.play.chat.exception;

import org.springframework.http.HttpStatus;

public class ChatRoomException extends RuntimeException{
    public ChatRoomException() {
    }

    public ChatRoomException(String message) {
        super(message);
    }

    public ChatRoomException(String message, Long chatRoomId) {
        super(message);
    }

    public ChatRoomException(String message, HttpStatus httpStatus) {
        super(message);
    }
}
