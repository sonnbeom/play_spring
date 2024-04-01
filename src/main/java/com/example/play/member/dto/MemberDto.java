package com.example.play.member.dto;

import lombok.*;


@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class MemberDto {
    private String name;
    private String email;
    private String password;
    private String nickname;
}
