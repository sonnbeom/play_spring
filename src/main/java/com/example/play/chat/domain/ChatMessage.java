package com.example.play.chat.domain;

import com.example.play.chat.dto.ChatDtoUpdate;
import com.example.play.chat.dto.ChatMessageDto;
import com.example.play.chat.dto.ChatMessageResponseDto;
import com.example.play.chat.dto.ChatRoomsWithChatsDto;
import com.example.play.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Optional;

@NoArgsConstructor
@Entity
@Slf4j
public class ChatMessage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nickname;
    private String msg;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    @Builder
    public ChatMessage(ChatMessageDto chatMessageDto, ChatRoom chatRoom){
        this.nickname = chatMessageDto.getNickname();
        this.msg = chatMessageDto.getMessage();
        this.chatRoom = chatRoom;
    }
    public ChatMessageResponseDto toDto(){
        return ChatMessageResponseDto.builder()
                .id(id)
                .msg(msg)
                .dateTime(getCreatedAt())
                .build();
    }

    public Long updateMsg(ChatDtoUpdate chatDto) {
        this.msg = chatDto.getMsg();
        return this.id;
    }

    public Optional<ChatRoomsWithChatsDto> findMatchingCharRoom(Map<ChatRoom, ChatRoomsWithChatsDto> map) {
        if (map.containsKey(chatRoom)){
            return Optional.of(map.get(chatRoom));
        }
        return Optional.empty();
    }
}
