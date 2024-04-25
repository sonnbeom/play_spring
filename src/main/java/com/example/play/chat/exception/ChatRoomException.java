package com.example.play.chat.exception;

public class ChatRoomException extends RuntimeException{
    public ChatRoomException() {
    }

    public ChatRoomException(String message) {
        super(message);
    }

    public ChatRoomException(String message, Long chatRoomId) {
        super(message);
    }
}
