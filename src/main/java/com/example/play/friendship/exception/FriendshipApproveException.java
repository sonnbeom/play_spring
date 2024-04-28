package com.example.play.friendship.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class FriendshipApproveException extends RuntimeException{
    @Getter
    private final HttpStatus status;

    public FriendshipApproveException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
