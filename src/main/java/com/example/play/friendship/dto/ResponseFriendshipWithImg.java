package com.example.play.friendship.dto;

import com.example.play.image.dto.ResponseImg;
import com.example.play.image.dto.ResponseMemberImg;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ResponseFriendshipWithImg {
    private ResponseFriendshipDto friendshipDto;
    private ResponseMemberImg img;
}
