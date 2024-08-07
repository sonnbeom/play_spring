package com.example.play.comment.dto;

import com.example.play.comment.domain.Comment;
import com.example.play.member.domain.Member;
import com.example.play.post.domain.Post;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Builder
@AllArgsConstructor
public class RequestCommentCreate {
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
