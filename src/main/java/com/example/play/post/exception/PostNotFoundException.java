package com.example.play.post.exception;

import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor
@Slf4j
public class PostNotFoundException extends RuntimeException{
    public PostNotFoundException(String message) {
        super(message);
        log.info(message);
    }
}
