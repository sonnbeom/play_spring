package com.example.play.like.post.exception;

import com.example.play.like.post.dto.RequestLike;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor
@Slf4j
public class PostLikeException extends RuntimeException{

    public PostLikeException(String message) {
        super(message);
        log.info(message);
    }

    public PostLikeException(String message, RequestLike likeRequest) {
        super(message +"likeRequest: " + likeRequest);
        log.info("{} likeRequest: {}", message, likeRequest);
    }
}
