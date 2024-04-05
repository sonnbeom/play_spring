package com.example.play.friendship.dto;

import com.example.play.friendship.constant.FriendshipStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseFriendListDto {
    private Long friendshipId;
    private String memberEmail;
    private Long memberId;
    private String friendEmail;
    private String friendNickname;
    private String imgUrl;
    private FriendshipStatus status;
    private LocalDateTime time;
}
