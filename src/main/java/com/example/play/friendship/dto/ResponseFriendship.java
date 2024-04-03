package com.example.play.friendship.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ResponseFriendship {
    private ResponseFriendshipDto toFriendship;
    private ResponseFriendshipDto fromFriendship;
}
