package com.example.play.jwt.service;

import com.example.play.jwt.dto.CustomUserDetails;
import com.example.play.member.entity.Member;
import com.example.play.member.exception.MemberNotFoundException;
import com.example.play.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {
    private final MemberRepository memberRepository;
    @Override
    public CustomUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new MemberNotFoundException("해당 이메일에 해당하는 인원을 찾을 수 없습니다: %s ", email));
        return new CustomUserDetails(member);
    }
}
