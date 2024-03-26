package com.example.play.post.controller;

import com.example.play.post.dto.CreatePostDto;
import com.example.play.post.dto.ResponsePostDto;
import com.example.play.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/post")
@Slf4j
public class PostController {
    private final PostService postService;
    /*
    * 단일 게시글 읽기
    * 리스트 보기
    * -> 좋아요순
    * -> 조회수
    * -> 최신순
    * 검색하기
    * 제목
    * 내용
    * 작성자
    * */
    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid CreatePostDto postDto,
                                                        BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(postDto);
        }
        ResponsePostDto response = postService.create(postDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @GetMapping("/{postId}")
    public ResponseEntity<ResponsePostDto> readOne(@PathVariable("postId") Long postId){
        ResponsePostDto responsePostDto = postService.readOne(postId);
        return ResponseEntity.status(HttpStatus.OK).body(responsePostDto);
    }
}
