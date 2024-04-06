package com.example.play.jwt.filter;

import com.example.play.jwt.util.JwtTokenUtil;
import com.example.play.member.entity.Member;
import com.example.play.member.service.MemberService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

//OncePerRequestFilter: 매번 들어갈 때마다 체크해주는 필터
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final MemberService memberService;
    private final String secretKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        // Header의 Authorization 값이 비어있으면 => Jwt Token을 전송하지 않음 => 로그인 하지 않음
        if (authorizationHeader == null){
            filterChain.doFilter(request, response);

         /*
        *filterChain.doFilter(request, response);는 서블릿 필터에서 사용되는 메소드로,
        * 현재 필터의 처리가 끝났음을 나타내고 요청을 필터 체인의 다음 필터로 넘기거나,
        * 필터 체인의 마지막이라면 실제 자원(서블릿, JSP 등)에 요청을 전달하는 역할을 합니다.
        *
        즉, 이 메소드는 HTTP 요청이 다음 목적지(다음 필터 또는 최종 자원)까지 계속 진행될 수 있도록 합니다.
        * 만약 이 메소드를 호출하지 않으면, 요청 처리가 중단되고, 클라이언트에게 어떠한 응답도 반환되지 않을 수 있습니다.
        * */

            return;
        }
        //Header의 Authorization 값이 Bearer로 시작하지 않으면  => 잘못된 토큰
        if (!authorizationHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }
        // 전송 받은 값에서 Bearer 뒷부분(Jwt Token) 추출
        String token = authorizationHeader.split(" ")[1];

        // 전송 받은 Jwt Token이 만료되었으면 => 다음 필터 진행(인증 x)
        if (JwtTokenUtil.isExpired(token)){
            filterChain.doFilter(request, response);
        }
        String loginId = JwtTokenUtil.getLoginId(token);
        Member loginMember = memberService.getLoginByMemberId(loginId);

        // 조회한 유저 정보를 바탕으로 객체 생성 사용자의 id, 비밀번호, 권한을 포함
        // 비밀번호를 null로 하는 이유는  이 시점에 비밀번호를 검증할 필요가 없기 때문 사용자가 로그인할 때 이미 비밃번호는 검증됨 + 보안상의 이유로 비밀번호와 같은 민감한 정보는 포함 x
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginMember.getId(), null, List.of(new SimpleGrantedAuthority(loginMember.getRole().name())));
        // 요청 객체로부터 인증 세부 정보를 생성 token에 설정
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        // SecurityContext에 인증 객체를 등록함으로써 현재 요청이 인증되었음을 시큐리티에 알림 애플리케이션 내 다른 부분에서 사용자의 인증 정보에 접근 가능
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        // 요청 처리 체인을 계속 진행
        filterChain.doFilter(request, response);
    }
}
