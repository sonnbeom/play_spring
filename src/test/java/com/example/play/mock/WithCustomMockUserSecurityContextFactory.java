package com.example.play.mock;

import com.example.play.jwt.dto.CustomUserDetails;
import com.example.play.member.domain.Member;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import static com.example.play.member.role.Role.ROLE_USER;

// CustomMockUser에서 제공한 데이터를 기반으로 WithSecurityContext를 생성하고 설정함
public class WithCustomMockUserSecurityContextFactory implements WithSecurityContextFactory<WithCustomMockUser> {
    @Override
    public SecurityContext createSecurityContext(WithCustomMockUser annotation) {
        String email = annotation.email();
        Member member = new Member("", email,  ROLE_USER,"");
        CustomUserDetails customUserDetails = new CustomUserDetails(member);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
        return securityContext;
    }
}
