package com.example.play.friendship.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FriendshipNotFoundException extends RuntimeException{
    public FriendshipNotFoundException() {
    }

    public FriendshipNotFoundException(String message) {
        super(message);
        log.info(message);
    }

    public FriendshipNotFoundException(String message, Long friendshipId) {
        super(String.format("%s :%d", message, friendshipId));
    }
}
