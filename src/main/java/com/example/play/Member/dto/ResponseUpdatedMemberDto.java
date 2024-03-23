package com.example.play.Member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseUpdatedMemberDto {
    private Long id;
    private String nickname;
}
