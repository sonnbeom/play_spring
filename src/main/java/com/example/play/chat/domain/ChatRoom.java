package com.example.play.chat.domain;

import com.example.play.chat.dto.ChatRoomDto;
import com.example.play.chat.dto.ChatRoomWithMessageDto;
import com.example.play.global.common.entity.BaseEntity;
import com.example.play.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@NoArgsConstructor
public class ChatRoom extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;
    @ManyToOne
    @JoinColumn(name = "my_id")
    private Member member;
    @ManyToOne
    @JoinColumn(name = "other_id")
    private Member other;
    @OneToMany(mappedBy = "chatRoom",cascade = CascadeType.REMOVE)
    private List<ChatMessage> chatMessages;


    @Builder
    public ChatRoom(Member fromMember, Member toMember) {
        this.member = fromMember;
        this.other = toMember;
    }

    public ChatRoomDto toDto() {
        return ChatRoomDto.builder()
                .chatRoomId(id)
                .nickName(member.getNicknameForChatRoomDto())
                .otherNickname(other.getNicknameForChatRoomDto())
                .build();
    }
}
