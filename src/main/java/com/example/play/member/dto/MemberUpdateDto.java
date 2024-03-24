package com.example.play.member.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class MemberUpdateDto {
    private String nickname;
    private String email;
    private String picture;
    private String password;
}
