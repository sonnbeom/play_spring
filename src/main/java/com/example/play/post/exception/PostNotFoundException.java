package com.example.play.post.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;


@Slf4j
public class PostNotFoundException extends RuntimeException{
    @Getter
    private final HttpStatus status;
    public PostNotFoundException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
