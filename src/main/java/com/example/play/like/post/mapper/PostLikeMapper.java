package com.example.play.like.post.mapper;

import com.example.play.like.post.dto.ResponseLike;
import com.example.play.like.post.entity.PostLike;
import com.example.play.member.entity.Member;
import com.example.play.post.entity.Post;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class PostLikeMapper {
    public PostLike createLike(Post post, Member member) {
        return PostLike.builder()
                .post(post)
                .member(member)
                .isActive(1)
                .build();
    }

    public ResponseLike entityToDto(PostLike savedLike) {
        return ResponseLike.builder()
                .id(savedLike.getId())
                .isActive(savedLike.getIsActive())
                .build();
    }
}
