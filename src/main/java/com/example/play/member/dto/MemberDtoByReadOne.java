package com.example.play.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class MemberDtoByReadOne {
    private String name;
    private String email;
    private String nickname;
    private String picture;

    @Builder
    public MemberDtoByReadOne(String name, String email, String nickname, String picture) {
        this.name = name;
        this.email = email;
        this.nickname = nickname;
        this.picture = picture;
    }
}
