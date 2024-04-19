package com.example.play.jwt.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class TokenDto {
    private String accessToken;
    private String refreshToken;
    private long refreshTokenExpirationTime;
}
