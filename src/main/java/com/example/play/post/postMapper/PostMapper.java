package com.example.play.post.postMapper;

import com.example.play.member.domain.Member;
import com.example.play.post.dto.RequestPostDto;
import com.example.play.post.dto.ResponsePostPageDto;
import com.example.play.post.dto.ResponsePostDTo;
import com.example.play.post.domain.Post;
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
    public Post dtoToEntity(RequestPostDto postDto, Member member) {
        return Post.builder()
                .hit(1)
                .title(postDto.getTitle())
                .content(postDto.getContent())
                .isActive(1)
                .member(member)
                .build();
    }
    public ResponsePostDTo pageEntityToDto(Page<Post> postPage) {
        int totalPages = postPage.getTotalPages();
        int currentPage = postPage.getNumber();
        List<ResponsePostPageDto> dtoList = postPage.getContent()
                .stream()
                .map(post -> post.toDtoForPage())
                .collect(Collectors.toList());
        return ResponsePostDTo.builder()
                .postListDto(dtoList)
                .totalPages(totalPages)
                .currentPage(currentPage)
                .build();
    }
}