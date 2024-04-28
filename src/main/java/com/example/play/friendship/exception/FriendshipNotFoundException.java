package com.example.play.friendship.exception;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
public class FriendshipNotFoundException extends RuntimeException{
    @Getter
    private final HttpStatus status;

    public FriendshipNotFoundException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
