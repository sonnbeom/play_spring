package com.example.play.member.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ResponseUpdatedMemberDto {
    private Long id;
    private String nickname;
}
