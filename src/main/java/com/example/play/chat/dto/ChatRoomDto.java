package com.example.play.chat.dto;

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
    private String nickName;
    private String otherNickname;
}
