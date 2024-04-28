package com.example.play.chat.exceptionhandler;

import com.example.play.chat.exception.ChatException;
import com.example.play.chat.exception.ChatRoomException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ChatExceptionHandler {
    @ExceptionHandler(ChatException.class)
    public ResponseEntity<String> handleChatException(ChatException ex){
        return new ResponseEntity<>(ex.getMessage(), ex.getStatus());
    }
    @ExceptionHandler(ChatRoomException.class)
    public ResponseEntity<String> handleChatRoomException(ChatRoomException ex){
        return new ResponseEntity<>(ex.getMessage(), ex.getStatus());
    }
}
