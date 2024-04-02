package com.example.play.like.post.repository;

import com.example.play.like.post.entity.PostLike;
import com.example.play.member.entity.Member;
import com.example.play.post.entity.Post;

import java.util.List;

public interface PostLikeCustomRepository {
    List<PostLike> findByPostAndMember(Post post, Member member);
}
