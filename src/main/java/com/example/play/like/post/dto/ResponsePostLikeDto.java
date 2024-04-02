package com.example.play.like.post.dto;

import com.example.play.post.dto.ResponsePostOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class ResponsePostLikeDto {
    private ResponseLike responseLike;
    private ResponsePostOne responsePostOne;
    private LikeStatus likeStatus;
    public enum LikeStatus{
        DELETE, CREATE
    }
}
