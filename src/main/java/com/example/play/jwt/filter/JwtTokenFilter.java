package com.example.play.jwt.filter;

import com.example.play.jwt.constant.HeaderConstant;
import com.example.play.jwt.util.JwtTokenUtil;
import com.example.play.member.entity.Member;
import com.example.play.member.service.MemberService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

//OncePerRequestFilter: 매번 들어갈 때마다 체크해주는 필터
@RequiredArgsConstructor
@Slf4j
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        Optional<String> accessToken = getToken(request, HttpHeaders.AUTHORIZATION);
        Optional<String> refreshToken = getToken(request, HeaderConstant.REFRESH_TOKEN);

        if (!accessToken.isEmpty() && jwtTokenUtil.tokenValidation(accessToken.get())){
            UsernamePasswordAuthenticationToken authentication = jwtTokenUtil.validateToken(accessToken.get());
//            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }
    private Optional<String> getToken(HttpServletRequest request, String headerName){
        String authorizationHeader = request.getHeader(headerName);

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            return Optional.ofNullable(authorizationHeader.split(" ")[1]);
        }else {
            return Optional.empty();
        }
    }
}
