package com.example.play.member.dto;

import com.example.play.image.dto.ResponseMemberImg;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
@AllArgsConstructor
@Builder
public class MemberDtoByReadOne {
    private Long id;
    private String name;
    private String email;
    private String nickname;
    private ResponseMemberImg img;

}
