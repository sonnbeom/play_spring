package com.example.play.friendship.dto;

import com.example.play.friendship.entity.Friendship;
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
    private Friendship.FriendshipStatus status;
    private Long counterpartId;
}
