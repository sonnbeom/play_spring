package com.example.play.chatroom.exceptionhandler;

import com.example.play.chatroom.exception.ChatRoomException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ChatRoomExceptionHandler {
    @ExceptionHandler(ChatRoomException.class)
    public ResponseEntity<String> handleChatRoomException(ChatRoomException ex){
        return new ResponseEntity<>(ex.getMessage(), ex.getStatus());
    }
}
