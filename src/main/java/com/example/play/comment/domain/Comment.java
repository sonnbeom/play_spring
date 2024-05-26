package com.example.play.comment.domain;

import com.example.play.comment.dto.RequestCommentUpdate;
import com.example.play.comment.dto.ResponseComment;
import com.example.play.comment.exception.CommentUpdateAuthorizationException;
import com.example.play.global.common.entity.BaseEntity;
import com.example.play.member.entity.Member;
import com.example.play.post.entity.Post;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
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

    public ResponseComment toDto() {
        return ResponseComment.builder()
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

    public void checkUpdateAuthorization(Member member) {
        if (!this.member.equals(member)){
            throw new CommentUpdateAuthorizationException("권한이 없는 멤버가 댓글 수정을 시도합니다", HttpStatus.FORBIDDEN);
        }
    }

    public void update(RequestCommentUpdate commentUpdate) {
        this.content = commentUpdate.getContent();
    }
}
