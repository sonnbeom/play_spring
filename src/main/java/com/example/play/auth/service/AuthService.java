package com.example.play.auth.service;

import com.example.play.jwt.dto.TokenDto;
import com.example.play.jwt.entity.RefreshToken;
import com.example.play.jwt.exception.RefreshTokenNotFoundException;
import com.example.play.jwt.repository.TokenRepository;
import com.example.play.jwt.util.JwtService;
import com.example.play.member.entity.Member;
import com.example.play.member.exception.MemberNotFoundException;
import com.example.play.member.repository.MemberRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;

    public Optional<TokenDto> refreshToken(String refreshToken, HttpServletResponse response) {
        TokenDto tokenDto = null;
        if (!ObjectUtils.isEmpty(refreshToken)){
            String email = jwtService.extractEmail(refreshToken);
            if (email != null){
                Member member = memberRepository.findByEmail(email).orElseThrow(()->new MemberNotFoundException("해당 email로 멤버를 찾을 수 없습니다", email));
                boolean isTokenValid = tokenRepository.findByEmailAndRefreshToken(member.getEmail(), refreshToken);
                if (jwtService.isTokenValid(refreshToken) && isTokenValid){
                    tokenDto = jwtService.provideToken(member.getEmail(), member.getRole());
                    RefreshToken refreshTokenEntity = new RefreshToken(tokenDto.getRefreshToken(), member.getEmail());
                    tokenRepository.save(refreshTokenEntity);
                }
            }
        }
        return tokenDto == null? Optional.empty() : Optional.of(tokenDto);
    }
}
