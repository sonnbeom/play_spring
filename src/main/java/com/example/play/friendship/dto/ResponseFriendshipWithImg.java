package com.example.play.friendship.dto;

import com.example.play.image.dto.ResponseImg;
import lombok.Builder;

@Builder
public class ResponseFriendshipWithImg {
    private ResponseFriendshipDto friendshipDto;
    private ResponseImg img;
}
