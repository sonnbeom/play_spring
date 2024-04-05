package com.example.play.friendship.dto;

import com.example.play.friendship.constant.FriendshipDeleteStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ResponseFriendshipDelete {
    private Long friendshipId;
    private Long counterpartId;
    private FriendshipDeleteStatus status;
}
