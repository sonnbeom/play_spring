package com.example.play.post.controller;

import com.example.play.jwt.dto.CustomUserDetails;
import com.example.play.post.dto.RequestPostDto;
import com.example.play.post.dto.ResponsePostOne;
import com.example.play.post.dto.ResponsePostDTo;
import com.example.play.post.dto.RequestUpdatePostDto;
import com.example.play.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<?> create(@Valid @RequestPart("postDto") RequestPostDto postDto,
                                    BindingResult bindingResult,
                                    @RequestPart(value = "files", required = false) List<MultipartFile> files,
                                    @AuthenticationPrincipal CustomUserDetails customUser){
        if (bindingResult.hasErrors()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(postDto);
        }
        ResponsePostOne response = postService.create(postDto ,files, customUser.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<ResponsePostOne> readOne(@PathVariable("postId") Long postId){
        ResponsePostOne responsePostDto = postService.readOne(postId);
        return ResponseEntity.status(HttpStatus.OK).body(responsePostDto);
    }
    //최신순 페이징 정렬
    @GetMapping("/sort")
    public ResponseEntity<ResponsePostDTo> readBySort(@RequestParam(defaultValue = "0")int page,
                                                      @RequestParam String sortType){
        ResponsePostDTo postResponseDto = postService.readBySort(page ,sortType);
        return ResponseEntity.status(HttpStatus.OK).body(postResponseDto);
    }
    @GetMapping("/search")
    public ResponseEntity<ResponsePostDTo> readBySearch(@RequestParam(defaultValue = "0") int page,
                                                        @RequestParam String type,
                                                        @RequestParam String keyword){
        ResponsePostDTo postResponseDto = postService.readBySearch(page, type, keyword);
        return ResponseEntity.status(HttpStatus.OK).body(postResponseDto);
    }
    // 게시글 작성자와 포스트 게시글의 매핑 작성자가 일치하여야 한다!
    @PatchMapping("/{postId}")
    public ResponseEntity<ResponsePostOne> update(@PathVariable("postId") Long postId,
                                                  @RequestPart(value = "updatePostDto") RequestUpdatePostDto updateDto,
                                                  @RequestPart(value = "files", required = false) List<MultipartFile> files,
                                                  @RequestPart(value = "deleteFileList", required = false) List<Long> deleteImageList){
        ResponsePostOne responseOne = postService.update(postId ,updateDto, files, deleteImageList);
        return ResponseEntity.status(HttpStatus.OK).body(responseOne);
    }
    // 게시글 작성자와 포스트 게시글의 매핑 작성자가 일치하여야 한다!
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
