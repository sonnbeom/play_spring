package com.example.play.auth.service;

import com.example.play.auth.dto.RequestLoginDto;
import com.example.play.jwt.dto.TokenDto;
import com.example.play.jwt.entity.RefreshToken;
import com.example.play.jwt.exception.RefreshTokenReissueException;
import com.example.play.jwt.repository.TokenRepository;
import com.example.play.jwt.service.JwtService;
import com.example.play.member.entity.Member;
import com.example.play.member.exception.MemberNotFoundException;
import com.example.play.member.repository.MemberRepository;
import com.example.play.member.service.MemberService;
import com.example.play.redis.service.RedisService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Optional;

import static com.example.play.jwt.constant.HeaderConstant.AUTHORIZATION;
import static com.example.play.jwt.constant.HeaderConstant.BEARER;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private final MemberService memberService;
    private final RedisService redisService;

    public TokenDto authorize(RequestLoginDto loginDto) {

        Member member = memberService.findByEmail(loginDto.getEmail());
        memberService.checkPassword(loginDto.getPassword(), member);

        TokenDto tokenDto = jwtService.provideToken(member.getEmail(), member.getRole());

        saveRefreshToken(member, tokenDto);

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

        String refreshToken_redis = redisService.getValues(member.getEmail());

        if (!refreshToken_redis.equals(refreshToken)){
            throw new RefreshTokenReissueException("Refresh token doesn't match", HttpStatus.BAD_REQUEST);
        }

        redisService.deleteValues(member.getEmail());

        tokenDto = jwtService.provideToken(member.getEmail(), member.getRole());
        saveRefreshToken(member, tokenDto);

        return tokenDto == null? Optional.empty() : Optional.of(tokenDto);
    }
    private void saveRefreshToken(Member member, TokenDto tokenDto){
        redisService.setValuesWithTimeOut(
                member.getEmail(),
                tokenDto.getRefreshToken(),
                tokenDto.getRefreshTokenExpirationTime());
    }

    public void logout(String email) {
        redisService.deleteValues(email);
    }
}
