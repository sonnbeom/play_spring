package com.example.play.friendship.dto;

import com.example.play.friendship.constant.FriendshipStatus;
import com.example.play.member.dto.ResponseMemberDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ResponseFriendshipDto {
    private Long friendshipId;
    private ResponseMemberDto senderDto;
    private ResponseMemberDto receiverDto;
    private FriendshipStatus status;
    private boolean isReceived;
}
