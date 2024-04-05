package com.example.play.friendship.exception;

public class FriendshipDeleteAuthorityException extends RuntimeException{
    public FriendshipDeleteAuthorityException(String message, Long memberId, Long friendshipId){
        super(String.format("%s member id: %d, friendshipId: %d ",message, memberId ,friendshipId));
    }
}
