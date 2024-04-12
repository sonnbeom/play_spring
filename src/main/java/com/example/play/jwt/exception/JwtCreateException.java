package com.example.play.jwt.exception;

import io.jsonwebtoken.JwtException;

public class JwtCreateException extends JwtException {
    public JwtCreateException(String message) {
        super(message);
    }
    public JwtCreateException(String message, String loginId, Throwable cause) {
        // 예외 메시지에 로그인 아이디와 원래 예외의 메시지를 포함시킵니다.
        super(String.format("%s 로그인 아이디: %s, 예외 메시지: %s", message, loginId, cause.getMessage()), cause);
    }
}
