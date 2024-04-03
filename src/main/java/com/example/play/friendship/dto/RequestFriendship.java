package com.example.play.friendship.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestFriendship {
    @NotBlank(message = "친구 신청 보내는 사람의 이메일은 필수입니다.")
    private String fromMemberEmail;
    @NotBlank(message = "친구 신청 받는 사람의 이메일은 필수입니다.")
    private String toMemberEmail;
}
