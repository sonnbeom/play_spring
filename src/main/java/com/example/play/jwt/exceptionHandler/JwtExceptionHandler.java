package com.example.play.jwt.exceptionHandler;

import com.example.play.jwt.exception.ErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class JwtExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<Object>expiredJwtHandler(ExpiredJwtException exception, WebRequest request){
        ErrorResponse errorResponse = new ErrorResponse("토큰이 만료되었습니다", "TOKEN_EXPIRED");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).
                header(HttpHeaders.WWW_AUTHENTICATE, "Bearer error=\"token_expired\"") //실제 값: Bearer error="token_expired"
                .body(errorResponse);
    }
}
