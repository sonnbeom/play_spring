package com.example.play.member.memberMapper;


import com.example.play.image.dto.ResponseMemberImg;
import com.example.play.member.dto.RequestMemberDto;
import com.example.play.member.dto.ResponseMemberDto;
import com.example.play.member.dto.ResponseDeleteMemberDto;
import com.example.play.member.entity.Member;
import com.example.play.member.role.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import static com.example.play.member.role.Role.*;

@RequiredArgsConstructor
@Component
@Slf4j
public class MemberMapper {

    private final PasswordEncoder passwordEncoder;
    public Member dtoToMember(RequestMemberDto memberDto){
        return Member.builder()
                .name(memberDto.getName())
                .email(memberDto.getEmail())
                .isActive(1)
                .password(passwordEncoder.encode(memberDto.getPassword()))
                .nickname(memberDto.getNickname())
                .role(ROLE_USER)
                .build();
    }

    public ResponseMemberDto entityToDto(Member member, ResponseMemberImg img) {
        return ResponseMemberDto.builder()
                .id(member.getId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .name(member.getName())
                .img(img)
                .build();
    }
    public ResponseDeleteMemberDto deleteResponse(int memberStatus, int imgStatus){
        return new ResponseDeleteMemberDto(memberStatus, imgStatus);
    }

}
