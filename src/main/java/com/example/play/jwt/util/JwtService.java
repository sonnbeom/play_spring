package com.example.play.jwt.util;

import com.example.play.jwt.dto.CustomUserDetails;
import com.example.play.jwt.dto.TokenDto;
import com.example.play.jwt.exception.InvalidJwtException;
import com.example.play.jwt.service.CustomUserDetailService;
import com.example.play.member.role.Role;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.Optional;

import static com.example.play.jwt.constant.HeaderConstant.*;
import static com.example.play.jwt.constant.HeaderConstant.BEARER;
import static com.example.play.jwt.constant.TokenTime.*;


@Slf4j
@Component
@RequiredArgsConstructor
public class JwtService {
    @Value("${secretKey}")
    private String key;
    private final CustomUserDetailService customUserDetailService;

    public String createToken(String email, Role role, long expireTimeMs){
        Claims claims = Jwts.claims();
        claims.put("EMAIL", email);
        claims.put("ROLE_TYPE", role);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireTimeMs))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }
    public TokenDto provideToken(String email, Role role){
        String accessToken = createToken(email, role, expireTimeMs);
        String refreshToken = createToken(email, role, refreshExpireTimeMs);
        return TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
    public String createAccessToken(String email, Role role){
        return createToken(email, role, expireTimeMs);
    }

    public UsernamePasswordAuthenticationToken validateToken(String token) {
        Claims claims = extractClaims(token);
        String email = claims.get("EMAIL").toString();

        CustomUserDetails userDetails = customUserDetailService.loadUserByUsername(email);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        return authentication;
    }

    // 밝급된 Token이 만료 시간이 지났는지 체크
//    public  boolean isExpired(String token) {
//        Date expiredDate = extractClaims(token).getExpiration();
//        // Token의 만료 날짜가 지금보다 이전인지 check
//        return expiredDate.before(new Date());
//    }
    public String extractEmail(String token){
       return extractClaims(token).get("EMAIL").toString();
    }

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
    public boolean isTokenValid(String token){
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

    public void setHeaderAccessToken(HttpServletResponse response, String accessToken) {
        response.addHeader(AUTHORIZATION, BEARER + accessToken);
    }
    public void setHeaderRefreshToken(HttpServletResponse response, String refreshToken) {
        response.addHeader(REFRESH_TOKEN, BEARER + refreshToken);
    }

}
