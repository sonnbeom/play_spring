package com.example.play.post.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostResponseOne {
    private Long id;
    private String title;
    private String content;
    private int hit;
    private int like;
    private LocalDateTime createdAt;
}
