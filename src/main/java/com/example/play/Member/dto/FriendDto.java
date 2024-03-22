package com.example.play.Member.dto;

import com.example.play.Member.constant.FriendshipStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FriendDto {
    private Long id;
    private String nickname;
    private FriendshipStatus status;
    private boolean isFrom;
}
