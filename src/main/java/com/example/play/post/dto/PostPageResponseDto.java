package com.example.play.post.dto;

import com.example.play.post.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostPageResponseDto {
    private Long id;
    private String title;
    private int likeCount;
    private int hit;
    public PostPageResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.likeCount = post.getLikeCount();
        this.hit = post.getHit();
    }
}
