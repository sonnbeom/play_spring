package com.example.play.comment.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ResponseCommentDto {
    private Long id;
    private String content;
    private String nickname;
    private LocalDateTime time;
    private Long parentId;

    @Builder
    public ResponseCommentDto(Long id, String content, String nickname, LocalDateTime time, Long parentId) {
        this.id = id;
        this.content = content;
        this.nickname = nickname;
        this.time = time;
        this.parentId = parentId;
    }
}
