package com.example.play.post.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponsePostDto {
    private Long id;
    private String title;
    private String content;
    private int hit;
    private int like;
}
