package com.example.play.member.memberMapper;


import com.example.play.member.dto.RequestCreateMemberDto;
import com.example.play.member.dto.ResponseDeleteMemberDto;
import com.example.play.member.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import static com.example.play.member.role.Role.*;

@RequiredArgsConstructor
@Component
@Slf4j
public class MemberMapper {

    private final PasswordEncoder passwordEncoder;
    public Member dtoToMember(RequestCreateMemberDto memberDto){
        return Member.builder()
                .name(memberDto.getName())
                .email(memberDto.getEmail())
                .isActive(1)
                .password(passwordEncoder.encode(memberDto.getPassword()))
                .nickname(memberDto.getNickname())
                .role(ROLE_USER)
                .build();
    }

    public ResponseDeleteMemberDto deleteResponse(int memberStatus, int imgStatus){
        return new ResponseDeleteMemberDto(memberStatus, imgStatus);
    }

}
