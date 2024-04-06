//package com.example.play.jwt.service;
//
//import com.example.play.jwt.dto.CustomUserDetails;
//import com.example.play.member.entity.Member;
//import com.example.play.member.repository.MemberRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class CustomUserDetailService implements UserDetailsService {
//    private final MemberRepository memberRepository;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Member memberData = memberRepository.findByName(username);
//
//        if (memberData != null){
//            return new CustomUserDetails(memberData);
//        }
//        return null;
//    }
//}
