package com.example.play.member.dto;

import com.example.play.image.dto.ResponseMemberImg;
import lombok.*;

@NoArgsConstructor
@Getter
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class ResponseMemberDto {
    private Long id;
    private String name;
    private String email;
    private String nickname;
    private ResponseMemberImg img;
}
