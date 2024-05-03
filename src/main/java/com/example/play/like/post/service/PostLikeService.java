package com.example.play.like.post.service;

import com.example.play.like.post.dto.RequestLike;
import com.example.play.like.post.dto.ResponsePostLikeDto;

public interface PostLikeService {

    // 좋아요 생성 이미 있으면 삭제
    ResponsePostLikeDto createLike(RequestLike likeRequest, String email);
}
