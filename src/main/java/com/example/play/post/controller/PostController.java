package com.example.play.post.controller;

import com.example.play.post.dto.CreatePostDto;
import com.example.play.post.dto.PostResponseOne;
import com.example.play.post.dto.PostResponseDto;
import com.example.play.post.dto.PostUpdateDto;
import com.example.play.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/post")
@Slf4j
public class PostController {
    private final PostService postService;

    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid @RequestPart("postDto") CreatePostDto postDto,
                                    BindingResult bindingResult,
                                    @RequestPart("files") List<MultipartFile> files){
        if (bindingResult.hasErrors()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(postDto);
        }
        PostResponseOne response = postService.create(postDto ,files);;
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponseOne> readOne(@PathVariable("postId") Long postId){
        PostResponseOne responsePostDto = postService.readOne(postId);
        return ResponseEntity.status(HttpStatus.OK).body(responsePostDto);
    }
    //최신순 페이징 정렬
    @GetMapping("/sort")
    public ResponseEntity<PostResponseDto> readBySort(@RequestParam(defaultValue = "0")int page, @RequestParam String sortType){
        PostResponseDto postResponseDto = postService.readBySort(page ,sortType);
        return ResponseEntity.status(HttpStatus.OK).body(postResponseDto);
    }
    @GetMapping("/search")
    public ResponseEntity<PostResponseDto> readBySearch(@RequestParam(defaultValue = "0") int page, @RequestParam String type, @RequestParam String keyword){
        PostResponseDto postResponseDto = postService.readBySearch(page, type, keyword);
        return ResponseEntity.status(HttpStatus.OK).body(postResponseDto);
    }
    @PatchMapping("/{postId}")
    public ResponseEntity<PostResponseOne> update(@PathVariable("postId") Long postId, @RequestBody PostUpdateDto updateDto){
        PostResponseOne responseOne = postService.update(postId ,updateDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseOne);
    }
    @DeleteMapping("/{postId}")
    public ResponseEntity delete(@PathVariable("postId")Long postId){
        int deleteSuccess = postService.delete(postId);
        if (deleteSuccess == 0){
            return ResponseEntity.status(HttpStatus.OK).build();
        }else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
