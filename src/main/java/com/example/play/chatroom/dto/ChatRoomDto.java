package com.example.play.chatroom.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Builder
@AllArgsConstructor
@Getter
public class ChatRoomDto {
    private Long chatRoomId;
    private String nickname;
    private String otherNickname;
}
