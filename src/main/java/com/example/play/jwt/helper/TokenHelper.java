//package com.example.play.jwt.helper;
//
//import com.example.play.jwt.exception.JwtCreateException;
//import com.example.play.jwt.util.JwtTokenUtil;
//import com.example.play.member.role.Role;
//import io.jsonwebtoken.JwtException;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class TokenHelper {
//    @Value("${secretKey}")
//    private String key;
//    @Value("${accessTokenValidTime}")
//    private long accessTokenValidTime;
//    @Value("${refreshTokenValidTime}")
//    private long refreshTokenValidTime;
//    @Getter
//    @AllArgsConstructor
//    public static class PrivateClaims{
//        private String memberEmail;
//        private Role role;
//    }
//    private JwtTokenUtil jwtTokenUtil;
//    public String createAccessToken(JwtTokenUtil.PrivateClaims privateClaims){
//        try {
//            return jwtTokenUtil.createToken(privateClaims, accessTokenValidTime);
//        } catch (JwtException e){
//            throw new JwtCreateException("access 토큰 생성 중 오류가 발생했습니다", privateClaims.getMemberEmail(), e);
//        }
//    }
//    public String createRefreshToken(JwtTokenUtil.PrivateClaims privateClaims){
//        try {
//            String token = createToken(privateClaims, refreshTokenValidTime);
//            saveRefreshToken(token);
//            return token;
//        } catch (JwtException e){
//            throw new JwtCreateException("refresh 토큰 생성 중 오류가 발생했습니다", privateClaims.getMemberEmail(), e);
//        }
//    }
//
//}
