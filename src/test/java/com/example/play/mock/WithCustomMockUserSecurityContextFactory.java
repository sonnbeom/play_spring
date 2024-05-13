package com.example.play.mock;

import com.example.play.jwt.dto.CustomUserDetails;
import com.example.play.member.entity.Member;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.List;
// CustomMockUser에서 제공한 데이터를 기반으로 WithSecurityContext를 생성하고 설정함
public class WithCustomMockUserSecurityContextFactory implements WithSecurityContextFactory<WithCustomMockUser> {
    @Override
    public SecurityContext createSecurityContext(WithCustomMockUser annotation) {
        String email = annotation.email();
        CustomUserDetails customUserDetails = new CustomUserDetails();
        UserDetails userDetails = Member.builder()

      //  Authentication authentication = new UsernamePasswordAuthenticationToken(email, "", List.of(new SimpleGrantedAuthority("ROLE_USER")));
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
        return securityContext;
    }
}
