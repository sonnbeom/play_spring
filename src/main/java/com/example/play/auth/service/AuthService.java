package com.example.play.auth.service;

import com.example.play.auth.dto.RequestLoginDto;
import com.example.play.jwt.dto.TokenDto;
import com.example.play.jwt.exception.RefreshTokenReissueException;
import com.example.play.jwt.service.JwtService;
import com.example.play.member.entity.Member;
import com.example.play.member.service.MemberService;
import com.example.play.redis.service.RedisService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtService jwtService;
    private final MemberService memberService;
    private final RedisService redisService;

    public TokenDto authorize(RequestLoginDto loginDto) {

        Member member = memberService.findByEmail(loginDto.getEmail());
        memberService.checkPassword(loginDto.getPassword(), member);

        TokenDto tokenDto = jwtService.provideToken(loginDto.getEmail(), member.getRoleForToken());

        saveRefreshToken(loginDto.getEmail(), tokenDto);

        return tokenDto;
    }

    public Optional<TokenDto> regenerateToken(String refreshToken, HttpServletResponse response) {
        TokenDto tokenDto = null;
        // cookie에서 가져온 refreshToken 체크
        if (ObjectUtils.isEmpty(refreshToken)){
            throw new RefreshTokenReissueException("Refresh Token is empty", HttpStatus.BAD_REQUEST);
        }

        String email = jwtService.extractEmail(refreshToken);
        Member member = memberService.findByEmail(email);

        String refreshToken_redis = redisService.getValues(email);

        if (!refreshToken_redis.equals(refreshToken)){
            throw new RefreshTokenReissueException("Redis에 있는 refreshToken과 클라이언트가 요구하는 refreshToken이 매칭되지 않습니다. ", HttpStatus.BAD_REQUEST);
        }

        redisService.deleteValues(email);

        tokenDto = jwtService.provideToken(email, member.getRoleForToken());
        saveRefreshToken(email, tokenDto);

        return tokenDto == null? Optional.empty() : Optional.of(tokenDto);
    }
    private void saveRefreshToken(String email, TokenDto tokenDto){
        redisService.setValuesWithTimeOut(
                email,
                tokenDto.getRefreshToken(),
                tokenDto.getRefreshTokenExpirationTime());
    }

    public void logout(String email) {
        redisService.deleteValues(email);
    }
}
