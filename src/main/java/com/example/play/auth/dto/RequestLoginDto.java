package com.example.play.auth.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class RequestLoginDto {
    private String email;
    private String password;
}
