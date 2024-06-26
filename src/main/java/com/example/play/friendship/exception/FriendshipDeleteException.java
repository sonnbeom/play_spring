package com.example.play.friendship.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class FriendshipDeleteException extends RuntimeException{
    @Getter
    private final HttpStatus status;

    public FriendshipDeleteException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
