package com.example.play.member.memberMapper;


import com.example.play.member.dto.MemberDto;
import com.example.play.member.dto.MemberDtoByReadOne;
import com.example.play.member.dto.ResponseUpdatedMemberDto;
import com.example.play.member.entity.Member;
import com.example.play.member.role.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class MemberMapper {
    @Autowired
    private final PasswordEncoder passwordEncoder;
    public Member dtoToMember(MemberDto memberDto){
        return Member.builder()
                .name(memberDto.getName())
                .email(memberDto.getEmail())
                .isActive(1)
                .password(passwordEncoder.encode(memberDto.getPassword()))
                .nickname(memberDto.getNickname())
                .role(Role.User)
                .picture(memberDto.getPicture())
                .build();
    }

    public MemberDtoByReadOne entityToDto(Member member) {
        return MemberDtoByReadOne.builder()
                .email(member.getEmail())
                .nickname(member.getNickname())
                .name(member.getName())
                .picture(member.getPicture())
                .build();
    }

    public ResponseUpdatedMemberDto updateMemberToDto(Member updateMember) {
        return ResponseUpdatedMemberDto.builder()
                .id(updateMember.getId())
                .nickname(updateMember.getNickname())
                .build();
    }
}
