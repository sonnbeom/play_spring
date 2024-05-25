package com.example.play.comment.dto;

import com.example.play.comment.domain.Comment;
import com.example.play.member.entity.Member;
import com.example.play.post.entity.Post;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor
@Getter
@Slf4j
public class RequestCommentDto {
    @NotNull
    private String content;
    private Long postId;
    private Long parentId;

    public Comment dtoToEntity(Post post, Member member) {
        return Comment.builder()
                .content(content)
                .post(post)
                .member(member)
                .build();
    }
    public boolean hasParenId(){
        if (parentId != null){
            return true;
        }
        return false;
    }

    public Comment dtoToEntityWithParent(Post post, Member member, Comment parent) {
        return Comment.builder()
                .content(content)
                .post(post)
                .member(member)
                .parent(parent)
                .build();
    }
}
