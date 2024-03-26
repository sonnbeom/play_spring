package com.example.play.post.postMapper;

import com.example.play.post.dto.CreatePostDto;
import com.example.play.post.dto.ResponsePostDto;
import com.example.play.post.entity.Post;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class PostMapper {
    public Post dtoToEntity(CreatePostDto postDto) {
        return Post.builder()
                .hit(0)
                .title(postDto.getTitle())
                .content(postDto.getContent())
                .isActive(1).build();
    }

    public ResponsePostDto entityToDto(Post saved) {
        return ResponsePostDto.builder()
                .id(saved.getId())
                .content(saved.getContent())
                .title(saved.getTitle())
                .hit(saved.getHit())
                .like(saved.getLikeCount())
                .build();
    }
}
