package com.example.play.jwt.controller;

import com.example.play.auth.service.AuthService;
import com.example.play.jwt.dto.TokenDto;
import com.example.play.jwt.util.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("api/vi/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final JwtService jwtService;

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@CookieValue String refreshToken, HttpServletResponse response) throws IOException {
        TokenDto tokenDto = null;
        Optional<TokenDto> optionalTokenDto = authService.refreshToken(refreshToken, response);
        if (optionalTokenDto.isPresent()){
           tokenDto = optionalTokenDto.get();
           jwtService.setHeaderAccessToken(response, tokenDto.getAccessToken());
            ResponseCookie responseCookie = ResponseCookie.from("refreshToken", tokenDto.getRefreshToken())
                    .httpOnly(true) // 이 옵션을 true로 설정하면, 쿠키가 HTTP(S)를 통해서만 접근 가능하게 됩니다. 즉, 클라이언트 사이드 스크립트(JS 등)에서는 document.cookie를 통해 이 쿠키에 접근할 수 없게 됩니다. 이는 XSS(Cross-site Scripting) 공격으로부터 쿠키를 보호하는 데 도움이 됩니다.
                    .secure(true) //  이 옵션을 true로 설정하면, 쿠키는 HTTPS 프로토콜을 사용하는 요청에서만 전송됩니다. 이는 쿠키가 암호화되지 않은 채널을 통해 전송되는 것을 방지하여, MITM(Man-In-The-Middle) 공격으로부터 보호합니다.
                    .path("/") // 쿠키가 전송될 경로를 지정한다. "/"로 설정하면 도메인의 모든 경로에 대해 쿠키가 전송된다.
                    .maxAge(604800) //7일
                    .domain("localhost") //이 옵션은 쿠키가 전송될 도메인을 지정합니다.
                    .build();
            return ResponseEntity.status(HttpStatus.OK)
                    .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                    .body(tokenDto);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(null);
    }

}

/*
@Operation(summary = "토큰 재발급", description = "토큰 만료시 재발급해줌. 리프레쉬 토큰은 쿠키에 담겨있음", tags = { "로그인" })
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@CookieValue String refreshToken, HttpServletResponse response) throws IOException {
        String newAccessToken = "";
        Optional<String> refreshedAccessToken = authService.refreshToken(refreshToken, response);
        if (refreshedAccessToken.isPresent()) {
            newAccessToken = refreshedAccessToken.get();
        }
        return ResponseEntity.ok()
            .body(new AuthResponse(newAccessToken));
    }
    }*/