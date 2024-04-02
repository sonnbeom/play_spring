package com.example.play.image.exception;

import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor
@Slf4j
public class MemberImgException extends RuntimeException{

    public MemberImgException(String message) {
        super(message);
        log.info("member img upload error: {}" , message);
    }
}
