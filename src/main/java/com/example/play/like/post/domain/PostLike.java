package com.example.play.like.post.domain;

import com.example.play.global.common.entity.BaseEntity;
import com.example.play.like.post.dto.ResponseLike;
import com.example.play.member.domain.Member;
import com.example.play.post.domain.Post;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostLike extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    @Column(name = "is_active")
    private int isActive;

    public int deleteLike() {
        isActive = 0;
        return isActive;
    }
    public ResponseLike toDto() {
        return ResponseLike.builder()
                .id(id)
                .isActive(isActive)
                .build();
    }
}
