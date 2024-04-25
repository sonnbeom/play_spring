package com.example.play.member.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Data
@Getter
@Setter
public class RequestMemberUpdateDto {
    private String nickname;
    private String email;
    private List<Long> imgIds;
    private String password;
}
