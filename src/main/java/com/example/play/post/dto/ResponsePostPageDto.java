package com.example.play.post.dto;

import com.example.play.image.entity.PostImage;
import com.example.play.post.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

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
