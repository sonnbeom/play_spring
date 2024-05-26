package com.example.play.comment.dto;

import lombok.Getter;

@Getter
public class RequestCommentUpdate {
    private Long commentId;
    private String content;
}
