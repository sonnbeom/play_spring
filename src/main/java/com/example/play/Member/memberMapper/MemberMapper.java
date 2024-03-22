package com.example.play.Member.memberMapper;


import com.example.play.Member.dto.MemberDto;
import com.example.play.Member.dto.MemberDtoByReadOne;
import com.example.play.Member.entity.Member;
import com.example.play.Member.role.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MemberMapper {
    PasswordEncoder passwordEncoder;
    public Member dtoToMember(MemberDto memberDto){
        return Member.builder()
                .name(memberDto.getName())
                .email(memberDto.getEmail())
                .isActive(1)
                .password(passwordEncoder.encode(memberDto.getPassword()))
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
}
