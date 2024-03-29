package com.example.play.post.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private List<String> urls = new ArrayList<>();
    private LocalDateTime createdAt;
}
