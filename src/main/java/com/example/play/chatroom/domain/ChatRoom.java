package com.example.play.chatroom.domain;

import com.example.play.chat.domain.ChatMessage;
import com.example.play.chatroom.dto.ChatRoomDto;
import com.example.play.global.common.entity.BaseEntity;
import com.example.play.member.domain.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ChatRoom extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "my_id")
    private Member member;
    @ManyToOne
    @JoinColumn(name = "other_id")
    private Member other;
    @OneToMany(mappedBy = "chatRoom",cascade = CascadeType.REMOVE)
    private List<ChatMessage> chatMessages;
    public ChatRoomDto toDto() {
        return ChatRoomDto.builder()
                .chatRoomId(id)
                .nickname(member.getNicknameForChatRoomDto())
                .otherNickname(other.getNicknameForChatRoomDto())
                .build();
    }
}
