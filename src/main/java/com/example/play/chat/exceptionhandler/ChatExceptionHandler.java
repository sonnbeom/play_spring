package com.example.play.chat.exceptionhandler;

import com.example.play.chat.exception.ChatException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ChatExceptionHandler {
    @ExceptionHandler(ChatException.class)
    public ResponseEntity<String> handleChatException(ChatException ex){
        return new ResponseEntity<>(ex.getMessage(), ex.getStatus());
    }
}
