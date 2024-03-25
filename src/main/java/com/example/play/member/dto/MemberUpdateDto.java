package com.example.play.member.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Data
@Getter
@Setter
public class MemberUpdateDto {
    private String nickname;
    private String email;
    private String picture;
    private String password;
}
