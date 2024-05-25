package com.example.play.chatroom.exceptionhandler;

import com.example.play.chatroom.exception.ChatRoomException;
import com.example.play.chatroom.exception.ChatRoomNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ChatRoomExceptionHandler {
    @ExceptionHandler(ChatRoomException.class)
    public ResponseEntity<String> handleChatRoomExceptionHandler(ChatRoomException ex){
        return new ResponseEntity<>(ex.getMessage(), ex.getStatus());
    }
    @ExceptionHandler(ChatRoomNotFoundException.class)
    public ResponseEntity<String> handleChatRoomNotFoundExceptionHandler(ChatRoomNotFoundException ex){
        return new ResponseEntity<>(ex.getMessage(), ex.getStatus());
    }
}
