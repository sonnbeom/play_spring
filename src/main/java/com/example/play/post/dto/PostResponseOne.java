package com.example.play.post.dto;

import com.example.play.image.dto.ResponseImg;
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
    private List<ResponseImg> responseImgs = new ArrayList<>();
    private LocalDateTime createdAt;
}
