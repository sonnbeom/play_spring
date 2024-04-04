package com.example.play.friendship.dto;

import com.example.play.friendship.constant.FriendshipStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ResponseFriendshipDto {
    private Long id;
    private boolean isFrom;
    private String memberEmail;
    private String friendEmail;
    private FriendshipStatus status;
    private Long counterpartId;
    private String friendNickname;
}
