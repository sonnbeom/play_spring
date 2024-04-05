package com.example.play.friendship.exception;

public class FriendshipDeleteException extends RuntimeException{

    public FriendshipDeleteException(String message) {
        super(message);
    }

    public FriendshipDeleteException(String message, Throwable cause) {
        super(message, cause);
    }
}
