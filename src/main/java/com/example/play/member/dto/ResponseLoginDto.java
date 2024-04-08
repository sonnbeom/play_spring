package com.example.play.member.dto;

import com.example.play.member.role.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Builder
@AllArgsConstructor
@Data
public class ResponseLoginDto {
    private Long id;
    private String name;
    private String email;
    private String password;
    private Integer isActive;
    private String nickname;
    private Role role;
    private boolean loginSuccess;
    private String jwtToken;
}
