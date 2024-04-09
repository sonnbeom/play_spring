package com.example.play.jwt.util;

import com.example.play.jwt.entity.RefreshToken;
import com.example.play.jwt.exception.InvalidJwtException;
import com.example.play.jwt.exception.JwtCreateException;
import com.example.play.jwt.repository.TokenRepository;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

import static com.example.play.jwt.constant.HeaderConstant.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenUtil {
    @Value("${secretKey}")
    private String key;
    @Value("${accessTokenValidTime}")
    private long accessTokenValidTime;
    @Value("${refreshTokenValidTime}")
    private long refreshTokenValidTime;

    private final TokenRepository tokenRepository;

    // jwt 토큰 생성
    private String createToken(String loginId, long tokenValid){
        // Claim : jwt token에 들어갈 정보
        // claim에 loginId를 넣음으로써 나중에 loginId 꺼낼 수 있음
            Claims claims = Jwts.claims();
            claims.put("loginId", loginId);
            return Jwts.builder()
                    .setClaims(claims)
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + tokenValid))
                    .signWith(SignatureAlgorithm.HS256, key)
                    .compact();

    }
    public String createAccessToken(String loginId){
        try {
            return createToken(loginId, accessTokenValidTime);
        } catch (JwtException e){
            throw new JwtCreateException("access 토큰 생성 중 오류가 발생했습니다", loginId, e);
        }
    }
    public String createRefreshToken(String loginId){
        try {
            String token = createToken(loginId, refreshTokenValidTime);
            saveRefreshToken(token);
            return token;
        } catch (JwtException e){
            throw new JwtCreateException("refresh 토큰 생성 중 오류가 발생했습니다", loginId, e);
        }
    }

    // claim에서 loginId 꺼내기
    public  String getLoginId(String token){
        return extractClaims(token).get("loginId").toString();
    }

    // 밝급된 Token이 만료 시간이 지났는지 체크
    public  boolean isExpired(String token) {
        Date expiredDate = extractClaims(token).getExpiration();
        // Token의 만료 날짜가 지금보다 이전인지 check
        return expiredDate.before(new Date());
    }

    // SecretKey를 사용해 Token Parsing
    private Claims extractClaims(String token) {
        try {
            return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
        } catch (SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
            throw new InvalidJwtException("잘못된 JWT 서명입니다.", e);
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
            throw new InvalidJwtException("만료된 JWT 토큰입니다.", e);
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
            throw new InvalidJwtException("지원되지 않는 JWT 토큰입니다.", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
            throw new InvalidJwtException("JWT 토큰이 잘못되었습니다." ,e);
        }
    }

    // 토큰의 유효성 검증을 수행
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(key).parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }
    public void setHeaderAccessToken(HttpServletResponse response, String accessToken){
        response.setHeader(AUTHORIZATION, BEARER_TOKEN + accessToken);
    }
    public void setHeaderRefreshToken(HttpServletResponse response, String refreshToken){
        response.setHeader(REFRESH_TOKEN, BEARER_TOKEN + refreshToken);
    }
    public boolean existsRefreshToken(String refreshToken){
        return tokenRepository.existsByRefreshToken(refreshToken);
    }
    private void saveRefreshToken(String token){
        RefreshToken refreshToken = new RefreshToken(token);
        tokenRepository.save(refreshToken);
    }
}
