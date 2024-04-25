package com.example.play.image.exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;



public class MemberImgException extends RuntimeException{
    @Getter
    private final HttpStatus status;
    public MemberImgException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
