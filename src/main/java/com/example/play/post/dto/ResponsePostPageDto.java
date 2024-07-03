package com.example.play.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponsePostPageDto {
    private Long id;
    private String title;
    private int likeCount;
    private int hit;
    private String url;

}
