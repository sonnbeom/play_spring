package com.example.play.member.dto;

import lombok.*;


@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class RequestCreateMemberDto {
    private String name;
    private String email;
    private String password;
    private String nickname;
}
