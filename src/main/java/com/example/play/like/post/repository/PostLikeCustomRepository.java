package com.example.play.like.post.repository;

import com.example.play.like.post.domain.PostLike;
import com.example.play.member.domain.Member;
import com.example.play.post.domain.Post;

import java.util.List;

public interface PostLikeCustomRepository {
    List<PostLike> findByPostAndMember(Post post, Member member);
}
