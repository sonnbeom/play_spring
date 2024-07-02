package com.example.play.like.post.mapper;

import com.example.play.like.post.domain.PostLike;
import com.example.play.member.domain.Member;
import com.example.play.post.domain.Post;
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
}
