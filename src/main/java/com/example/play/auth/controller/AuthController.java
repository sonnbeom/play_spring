package com.example.play.auth.controller;

import com.example.play.auth.dto.RequestLoginDto;
import com.example.play.auth.mapper.RefreshCookieMapper;
import com.example.play.auth.service.AuthService;
import com.example.play.jwt.dto.CustomUserDetails;
import com.example.play.jwt.dto.TokenDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;

import static com.example.play.jwt.constant.HeaderConstant.AUTHORIZATION;
import static com.example.play.jwt.constant.HeaderConstant.BEARER;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final RefreshCookieMapper refreshCookieMapper;

    @Operation(summary = "로그인", description = "로그인 후 JWT 발급하는 API입니다.")
    @ApiResponse(responseCode = "200", description = "로그인 후 JWT 발급에 성공했습니다.", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "404", description = "해당 이메일을 가진 멤버가 조회하지 않습니다.", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "400", description = "비밀번호가 일치하지 않습니다.", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "500", description = "로그인에 실패했습니다.", content = @Content(mediaType = "application/json"))
    @PostMapping("/authenticate")
    public ResponseEntity<TokenDto> authenticate(@RequestBody RequestLoginDto loginDto,
                                          HttpServletResponse response){
        TokenDto tokenDto = authService.authorize(loginDto);

        response.addHeader(AUTHORIZATION, BEARER + tokenDto.getAccessToken());

        ResponseCookie refreshCookie = refreshCookieMapper.responseCookie(tokenDto.getRefreshToken());

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(tokenDto);
    }
    @Operation(summary = "리프레시 토큰 재발급", description = "Access Token이 만료되어 Refresh Token을 재발급하는 API입니다.")
    @ApiResponse(responseCode = "200", description = "Refresh Token을 기반으로 AccessToken,Refresh Token 재발급에 성공했습니다.", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "400", description = "Refresh Token이 존재하지 않습니다.", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "400", description = "Redis에 있는 Refresh Token과 클라이언트가 요구하는 Refresh Token이 매칭되지 않습니다..", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "404", description = "조회한 멤버가 존재하지 않습니다.", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "500", description = "Refresh Token 재발급에 실패했습니다.", content = @Content(mediaType = "application/json"))
    @PostMapping("/reissue")
    public ResponseEntity<?> refreshToken(@CookieValue String refreshToken,
                                          HttpServletResponse response) throws IOException {
        TokenDto tokenDto = null;
        Optional<TokenDto> optionalTokenDto = authService.regenerateToken(refreshToken, response);
        if (optionalTokenDto.isPresent()){
            tokenDto = optionalTokenDto.get();

            response.addHeader(AUTHORIZATION, BEARER + tokenDto.getAccessToken());

            ResponseCookie refreshCookie = refreshCookieMapper.responseCookie(tokenDto.getRefreshToken());

            return ResponseEntity.status(HttpStatus.OK)
                    .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                    .body(tokenDto);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(null);
    }
    /*
    * 로그아웃 했는데 accessToken -> 접근 어케 막을 건지
    * 로그아웃 시에 redis black list에 올린다?
    * 장점: 로그아웃 하고 나서 accessToken을 이용해서 접근할 수 없다
    * 단점: doFilter 메소드가 실행될 때마다 redis를 확인한다.
    * */
    @Operation(summary = "로그 아웃", description = "로그아웃 API입니다.")
    @ApiResponse(responseCode = "200", description = "로그 아웃 및 쿠키에 Refresh Token을 삭제하였습니다.", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "500", description = "로그 아웃 및 쿠키에 Refresh Token을 삭제하는 것에 실패했습니다.", content = @Content(mediaType = "application/json"))
    @GetMapping("/logout")
    public ResponseEntity<?> logout(@AuthenticationPrincipal CustomUserDetails userDetails){
        authService.logout(userDetails.getUsername());
        ResponseCookie deleteCookie = refreshCookieMapper.deleteCookie();
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, deleteCookie.toString())
                .build();
    }
    @GetMapping()
    public String socialLogin(){
        return "login";
    }

}
