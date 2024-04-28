package com.example.play.friendship.dto;

import com.example.play.image.dto.ResponseImg;
import com.example.play.image.dto.ResponseMemberImg;
import lombok.Builder;

@Builder
public class ResponseFriendshipWithImg {
    private ResponseFriendshipDto friendshipDto;
    private ResponseMemberImg img;
}
