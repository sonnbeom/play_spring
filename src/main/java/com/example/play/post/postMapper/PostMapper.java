package com.example.play.post.postMapper;

import com.example.play.image.dto.ResponseImg;
import com.example.play.post.dto.RequestPostDto;
import com.example.play.post.dto.ResponsePostPageDto;
import com.example.play.post.dto.ResponsePostDTo;
import com.example.play.post.dto.ResponsePostOne;
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
    public Post dtoToEntity(RequestPostDto postDto) {
        return Post.builder()
                .hit(1)
                .title(postDto.getTitle())
                .content(postDto.getContent())
                .isActive(1).build();
    }

    public ResponsePostOne entityToDtoWithImage(Post saved, List<ResponseImg> responseImgs) {
        return ResponsePostOne.builder()
                .id(saved.getId())
                .content(saved.getContent())
                .title(saved.getTitle())
                .hit(saved.getHit()+1)
                .responseImgs(responseImgs)
                .like(saved.getLikeCount())
                .createdAt(saved.getCreatedAt())
                .build();
    }
    public ResponsePostOne entityToDto(Post saved) {
        return ResponsePostOne.builder()
                .id(saved.getId())
                .content(saved.getContent())
                .title(saved.getTitle())
                .hit(saved.getHit())
                .like(saved.getLikeCount())
                .createdAt(saved.getCreatedAt())
                .build();
    }

    public ResponsePostDTo pageEntityToDto(Page<Post> postPage) {
        int totalPages = postPage.getTotalPages();
        int currentPage = postPage.getNumber();
        List<ResponsePostPageDto> dtoList = postPage.getContent()
                .stream()
                .map(post -> new ResponsePostPageDto(post))
                .collect(Collectors.toList());
        return ResponsePostDTo.builder()
                .postListDto(dtoList)
                .totalPages(totalPages)
                .currentPage(currentPage)
                .build();
    }
}
