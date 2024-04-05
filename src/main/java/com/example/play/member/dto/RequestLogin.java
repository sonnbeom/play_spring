package com.example.play.member.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class RequestLogin {
    private Long memberId;
    private String password;
}
