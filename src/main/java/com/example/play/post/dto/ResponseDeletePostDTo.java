package com.example.play.post.dto;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ResponseDeletePostDTo {
    private Long id;
    private DeleteStatus status;
    private String content;
    private int hit;
    private int isActive;
    private int likeCount;
    private String title;
    public enum DeleteStatus{
        DELETED
    }
}