package com.example.play.Member.dto;

import com.example.play.Member.role.Role;
import lombok.*;


@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class MemberDto {
    private String name;
    private String email;
    private String password;
    private String phone;
    private String nickname;
    private String picture;
}
