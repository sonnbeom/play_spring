package com.example.play.post.postMapper;

import com.example.play.post.dto.CreatePostDto;
import com.example.play.post.dto.PostPageResponseDto;
import com.example.play.post.dto.PostResponseDto;
import com.example.play.post.dto.PostResponseOne;
import com.example.play.post.entity.Post;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

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

    public PostResponseOne entityToDto(Post saved) {
        return PostResponseOne.builder()
                .id(saved.getId())
                .content(saved.getContent())
                .title(saved.getTitle())
                .hit(saved.getHit())
                .like(saved.getLikeCount())
                .build();
    }

    public PostResponseDto pageEntityToDto(Page<Post> postPage) {
        int totalPages = postPage.getTotalPages();
        int currentPage = postPage.getNumber();
        List<PostPageResponseDto> dtoList = postPage.getContent()
                .stream()
                .map(post -> new PostPageResponseDto(post))
                .collect(Collectors.toList());
        return PostResponseDto.builder()
                .postListDto(dtoList)
                .totalPages(totalPages)
                .currentPage(currentPage)
                .build();
    }
}
