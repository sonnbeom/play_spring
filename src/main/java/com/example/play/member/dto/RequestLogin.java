package com.example.play.member.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class RequestLogin {
    private String memberEmail;
    private String password;
}
