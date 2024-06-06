package com.example.play.post.controller;

import com.example.play.jwt.dto.CustomUserDetails;
import com.example.play.post.dto.*;
import com.example.play.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/post")
@Slf4j
public class PostController {
    private final PostService postService;

    @PostMapping()
    public ResponseEntity<ResponsePostOne> create(@Valid @RequestPart("postDto") RequestPostDto postDto,
                                    @RequestPart(value = "files", required = false) List<MultipartFile> files,
                                    @AuthenticationPrincipal CustomUserDetails customUser){

        ResponsePostOne response = postService.create(postDto ,files, customUser.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<ResponsePostOne> getOne(@PathVariable("postId") Long postId){
        ResponsePostOne responsePostDto = postService.readOne(postId);
        return ResponseEntity.status(HttpStatus.OK).body(responsePostDto);
    }
    @GetMapping("/likeList")
    public ResponseEntity<ResponsePostDTo> readLikedPosts(@AuthenticationPrincipal CustomUserDetails userDetails,
                                            @RequestParam(defaultValue = "0") int page){
        ResponsePostDTo responsePostDTo = postService.getLikedPosts(userDetails.getUsername(), page);
        return ResponseEntity.status(HttpStatus.OK).body(responsePostDTo);
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
    @PatchMapping("/{postId}")
    public ResponseEntity<ResponsePostOne> update(@PathVariable("postId") Long postId,
                                                  @AuthenticationPrincipal CustomUserDetails userDetails,
                                                  @RequestPart(value = "updatePostDto") RequestUpdatePostDto updateDto,
                                                  @RequestPart(value = "files", required = false) List<MultipartFile> files,
                                                  @RequestPart(value = "deleteFileList", required = false) List<Long> deleteImageList){
        ResponsePostOne responseOne = postService.update(postId ,updateDto, files, deleteImageList, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.OK).body(responseOne);
    }
    @DeleteMapping("/{postId}")
    public ResponseEntity<ResponseDeletePostDTo> delete(@PathVariable("postId")Long postId,
                                          @AuthenticationPrincipal CustomUserDetails userDetails){
        ResponseDeletePostDTo result = postService.delete(postId, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
