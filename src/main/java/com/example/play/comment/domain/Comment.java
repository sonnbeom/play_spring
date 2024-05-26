package com.example.play.comment.domain;

import com.example.play.comment.dto.ResponseCommentDto;
import com.example.play.global.common.entity.BaseEntity;
import com.example.play.member.entity.Member;
import com.example.play.post.entity.Post;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Slf4j
@Getter
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Comment parent;
    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    private List<Comment> children = new ArrayList<>();

    @Builder
    public Comment(String content, Member member, Post post, Comment parent) {
        this.content = content;
        this.member = member;
        this.post = post;
        this.parent = parent;
    }

    public ResponseCommentDto toDto() {
        return ResponseCommentDto.builder()
                .time(getCreatedAt())
                .id(id)
                .content(content)
                .nickname(member.getNicknameForComment())
                .parentId(parentCheck())
                .build();
    }
    private Long parentCheck(){
        if (parent == null){
            return null;
        }
        else{
            return parent.id;
        }
    }
}
