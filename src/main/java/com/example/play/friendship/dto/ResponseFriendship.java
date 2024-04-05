package com.example.play.friendship.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ResponseFriendship {
    private ResponseFriendshipDto toFriendship;
    private ResponseFriendshipDto fromFriendship;
}
