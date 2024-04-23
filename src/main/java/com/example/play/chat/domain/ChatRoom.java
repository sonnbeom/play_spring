package com.example.play.chat.domain;

import com.example.play.global.common.entity.BaseEntity;
import com.example.play.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
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

    @Builder
    public ChatRoom(Member fromMember, Member toMember) {
        this.member = fromMember;
        this.other = toMember;
    }
}
