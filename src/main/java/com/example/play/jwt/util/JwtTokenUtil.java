package com.example.play.jwt.util;

import com.example.play.jwt.dto.CustomUserDetails;
import com.example.play.jwt.entity.RefreshToken;
import com.example.play.jwt.exception.InvalidJwtException;
import com.example.play.jwt.exception.JwtCreateException;
import com.example.play.jwt.repository.TokenRepository;
import com.example.play.jwt.service.CustomUserDetailService;
import com.example.play.member.role.Role;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
    private CustomUserDetailService customUserDetailService;

    @Getter
    @AllArgsConstructor
    public static class PrivateClaims{
        private String memberEmail;
        private Role role;
    }
    // jwt 토큰 생성
    private String createToken(PrivateClaims privateClaims, long tokenValid){
        // Claim : jwt token에 들어갈 정보
        // claim에 loginId를 넣음으로써 나중에 loginId 꺼낼 수 있음
//            Claims claims = Jwts.claims();
        Map<String, Object> claim = convert(privateClaims);
        log.info("tokenValid: {}",tokenValid);
            return Jwts.builder()
                    .setClaims(claim)
                    .setIssuedAt(new Date(System.currentTimeMillis()))
//                    .addClaims(convert)
                    .setExpiration(new Date(System.currentTimeMillis() + tokenValid))
                    .signWith(SignatureAlgorithm.HS256, key)
                    .compact();
    }
//    private PrivateClaims convert(Claims claims) {
//        return new PrivateClaims(claims.get(MEMBER_ID, String.class), claims.get(ROLE_TYPES, UserRole.class));
//    }
    private Map<String, Object> convert(PrivateClaims privateClaims){
        Map<String, Object> map = new HashMap<>();
        map.put("email", privateClaims.getMemberEmail());
        map.put("role", privateClaims.getRole());
        return map;
    }
    public String createAccessToken(PrivateClaims privateClaims){
        try {
            return createToken(privateClaims, accessTokenValidTime);
        } catch (JwtException e){
            throw new JwtCreateException("access 토큰 생성 중 오류가 발생했습니다", privateClaims.getMemberEmail(), e);
        }
    }
    public String createRefreshToken(PrivateClaims privateClaims){
        try {
            String token = createToken(privateClaims, refreshTokenValidTime);
            saveRefreshToken(token);
            return token;
        } catch (JwtException e){
            throw new JwtCreateException("refresh 토큰 생성 중 오류가 발생했습니다", privateClaims.getMemberEmail(), e);
        }
    }

    // claim에서 loginId 꺼내기
    public String getLoginId(String token){
        return extractClaims(token).get("loginId").toString();
    }
    public String getEmail(String token){
        return extractClaims(token).get("email").toString();
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
            log.info("만료시간좀 보자 :{}",Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody().getExpiration());
            return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
        } catch (SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
            throw new InvalidJwtException("잘못된 JWT 서명입니다.", e);
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
            log.info("토큰 만료 시간: {}", e.getClaims().getExpiration());
            log.info("뭐가 문제고", e.getMessage(), e.getCause());
            throw new InvalidJwtException("만료된 JWT 토큰입니다.", e);
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
            throw new InvalidJwtException("지원되지 않는 JWT 토큰입니다.", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
            throw new InvalidJwtException("JWT 토큰이 잘못되었습니다." ,e);
        }
    }
    public Authentication validateToken(String token) {
        Claims claims = extractClaims(token);
        UsernamePasswordAuthenticationToken authentication = getAuthentication(claims);
        return authentication;
    }

    private UsernamePasswordAuthenticationToken getAuthentication(Claims claims) {
        CustomUserDetails userDetails = customUserDetailService.loadUserByUsername(claims.get("email").toString());
        return new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getAuthorities());
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

// 토큰의 유효성 검증을 수행
//    public Authentication validateToken(HttpServletRequest request, String token) {
//        try {
//            Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
//            return getAuthentication(token);
//        } catch (SecurityException | MalformedJwtException e) {
//            log.info("잘못된 JWT 서명입니다.");
//        } catch (ExpiredJwtException e) {
//            log.info("만료된 JWT 토큰입니다.");
//        } catch (UnsupportedJwtException e) {
//            log.info("지원되지 않는 JWT 토큰입니다.");
//        } catch (IllegalArgumentException e) {
//            log.info("JWT 토큰이 잘못되었습니다.");
//        }
//        return null;
//    }