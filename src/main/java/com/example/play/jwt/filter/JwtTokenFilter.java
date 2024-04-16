package com.example.play.jwt.filter;

import com.example.play.jwt.util.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

//OncePerRequestFilter: 매번 들어갈 때마다 체크해주는 필터
@RequiredArgsConstructor
@Slf4j
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        Optional<String> accessToken = getToken(request, HttpHeaders.AUTHORIZATION);

        if (!accessToken.isEmpty() && jwtService.isTokenValid(accessToken.get())){
            UsernamePasswordAuthenticationToken authentication = jwtService.validateToken(accessToken.get());
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
