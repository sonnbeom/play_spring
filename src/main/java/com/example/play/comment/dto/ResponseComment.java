package com.example.play.comment.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ResponseComment {
    private Long id;
    private String content;
    private String nickname;
    private LocalDateTime time;
    private Long parentId;
    private List<ResponseComment> childList = new ArrayList<>();

    @Builder
    public ResponseComment(Long id, String content, String nickname, LocalDateTime time, Long parentId) {
        this.id = id;
        this.content = content;
        this.nickname = nickname;
        this.time = time;
        this.parentId = parentId;
    }
    public boolean haveParentId(){
        if (parentId == null){
            return false;
        }
        return true;
    }
}
