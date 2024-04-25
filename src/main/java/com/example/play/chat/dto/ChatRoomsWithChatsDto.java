package com.example.play.chat.dto;


import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ChatRoomsWithChatsDto {
    private ChatRoomDto chatRoomDto;
    private ChatMessageResponseDto chatMessage;
    @Builder
    public ChatRoomsWithChatsDto(ChatRoomDto chatRoomDto, ChatMessageResponseDto chatMessage) {
        this.chatRoomDto = chatRoomDto;
        this.chatMessage = chatMessage;
    }
    public void insertChatMessage(ChatMessageResponseDto chatMessage){
        this.chatMessage = chatMessage;
    }
}
