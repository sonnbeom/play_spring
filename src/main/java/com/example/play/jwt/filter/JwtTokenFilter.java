package com.example.play.jwt.filter;

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

        Optional<String> token = getToken(request, HttpHeaders.AUTHORIZATION);

        if (!token.isEmpty()){
            Authentication authentication = jwtTokenUtil.validateToken(token.get());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }else {
            filterChain.doFilter(request, response);
        }
        // 전송 받은 Jwt Token이 만료되었으면 => 다음 필터 진행(인증 x)
//        if (jwtTokenUtil.isExpired(token)){
//            filterChain.doFilter(request, response);
//        }
//        String loginId = jwtTokenUtil.getLoginId(token);
//        Member loginMember = memberService.getLoginByMemberId(loginId);

        // 조회한 유저 정보를 바탕으로 객체 생성 사용자의 id, 비밀번호, 권한을 포함
        // 비밀번호를 null로 하는 이유는  이 시점에 비밀번호를 검증할 필요가 없기 때문 사용자가 로그인할 때 이미 비밃번호는 검증됨 + 보안상의 이유로 비밀번호와 같은 민감한 정보는 포함 x
//        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
//                loginMember.getId(), null, List.of(new SimpleGrantedAuthority(loginMember.getRole().name())));
//        // 요청 객체로부터 인증 세부 정보를 생성 token에 설정
//        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//        // SecurityContext에 인증 객체를 등록함으로써 현재 요청이 인증되었음을 시큐리티에 알림 애플리케이션 내 다른 부분에서 사용자의 인증 정보에 접근 가능
//        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//        // 요청 처리 체인을 계속 진행
//        filterChain.doFilter(request, response);
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
