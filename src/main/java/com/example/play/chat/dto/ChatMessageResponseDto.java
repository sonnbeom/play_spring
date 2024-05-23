package com.example.play.chat.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ChatMessageResponseDto {
    private Long id;
    private String msg;
//    private ChatRoomDto chatRoomDto;
    private LocalDateTime dateTime;
}
