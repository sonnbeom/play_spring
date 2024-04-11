package com.example.play.jwt.service;

import com.example.play.jwt.dto.CustomUserDetails;
import com.example.play.member.entity.Member;
import com.example.play.member.exception.MemberNotFoundException;
import com.example.play.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;
    @Override
    public CustomUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return memberRepository.findByEmail(email)
                .map(this::createUserDetails)
                .orElseThrow(()->new MemberNotFoundException("해당 email을 가진 회원을 조회할 수 없습니다: {}",email));

    }
    private CustomUserDetails createUserDetails(Member member){
        return new CustomUserDetails(member);
    }
}
