package com.example.play.friendship.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class RequestDeleteFriendship {
    private Long friendshipId;
}
