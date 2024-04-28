package com.example.play.friendship.exceptionhandler;

import com.example.play.friendship.exception.FriendshipApproveException;
import com.example.play.friendship.exception.FriendshipDeleteException;
import com.example.play.friendship.exception.FriendshipNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class FriendshipExceptionHandler{
    @ExceptionHandler(FriendshipApproveException.class)
    public ResponseEntity<String> handleFriendshipApproveException(FriendshipApproveException ex){
        return new ResponseEntity<>(ex.getMessage(), ex.getStatus());
    }
    @ExceptionHandler(FriendshipDeleteException.class)
    public ResponseEntity<String> handleFriendshipDeleteException(FriendshipDeleteException ex){
        return new ResponseEntity<>(ex.getMessage(), ex.getStatus());
    }
    @ExceptionHandler(FriendshipNotFoundException.class)
    public ResponseEntity<String> handlerFriendshipNotFoundExceptionHandler(FriendshipNotFoundException ex){
        return new ResponseEntity<>(ex.getMessage(), ex.getStatus());
    }
}
