package com.example.play.post.controller;

import com.example.play.jwt.dto.CustomUserDetails;
import com.example.play.post.dto.*;
import com.example.play.post.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "게시글 생성", description = "제목과 내용을 기반으로 게시글 생성하는 API입니다.")
    @PostMapping()
    public ResponseEntity<ResponsePostOne> create(@Valid @RequestPart("postDto") RequestPostDto postDto,
                                    @RequestPart(value = "files", required = false) List<MultipartFile> files,
                                    @AuthenticationPrincipal CustomUserDetails customUser){

        ResponsePostOne response = postService.create(postDto ,files, customUser.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "게시글 조회", description = "id를 기반으로 게시글 조회하는 API입니다.")
    @GetMapping("/{postId}")
    public ResponseEntity<ResponsePostOne> getOne(@PathVariable("postId") Long postId){
        ResponsePostOne responsePostDto = postService.readOne(postId);
        return ResponseEntity.status(HttpStatus.OK).body(responsePostDto);
    }
    @Operation(summary = "좋아요한 게시글 리스트 조회", description = "좋아요한 게시글들을 불러오는 API입니다.")
    @GetMapping("/likeList")
    public ResponseEntity<ResponsePostDTo> readLikedPosts(@AuthenticationPrincipal CustomUserDetails userDetails,
                                            @RequestParam(defaultValue = "0") int page){
        ResponsePostDTo responsePostDTo = postService.getLikedPosts(userDetails.getUsername(), page);
        return ResponseEntity.status(HttpStatus.OK).body(responsePostDTo);
    }
    @Operation(summary = "타입에 따른 게시글 정렬", description = "좋아요순, 조회순, 최신순으로 게시글 정렬해서 가져오는 API입니다.")
    //최신순 페이징 정렬
    @GetMapping("/sort")
    public ResponseEntity<ResponsePostDTo> readBySort(@RequestParam(defaultValue = "0")int page,
                                                      @RequestParam String sortType){
        ResponsePostDTo postResponseDto = postService.readBySort(page ,sortType);
        return ResponseEntity.status(HttpStatus.OK).body(postResponseDto);
    }
    @Operation(summary = "게시글 검색", description = "제목, 작성자, 내용을 기반으로 게시글을 검색하는 API입니다.")
    @GetMapping("/search")
    public ResponseEntity<ResponsePostDTo> readBySearch(@RequestParam(defaultValue = "0") int page,
                                                        @RequestParam String type,
                                                        @RequestParam String keyword){
        ResponsePostDTo postResponseDto = postService.readBySearch(page, type, keyword);
        return ResponseEntity.status(HttpStatus.OK).body(postResponseDto);
    }
    @Operation(summary = "게시글 수정", description = "id를 기반으로 게시글 제목,내용,사진을 업데이트하는  API입니다.")
    @PatchMapping("/{postId}")
    public ResponseEntity<ResponsePostOne> update(@PathVariable("postId") Long postId,
                                                  @AuthenticationPrincipal CustomUserDetails userDetails,
                                                  @RequestPart(value = "updatePostDto") RequestUpdatePostDto updateDto,
                                                  @RequestPart(value = "files", required = false) List<MultipartFile> files,
                                                  @RequestPart(value = "deleteFileList", required = false) List<Long> deleteImageList){
        ResponsePostOne responseOne = postService.update(postId ,updateDto, files, deleteImageList, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.OK).body(responseOne);
    }
    @Operation(summary = "게시글 삭제", description = "id를 기반으로 게시글 삭제하는 API입니다.")
    @DeleteMapping("/{postId}")
    public ResponseEntity<ResponseDeletePostDTo> delete(@PathVariable("postId")Long postId,
                                          @AuthenticationPrincipal CustomUserDetails userDetails){
        ResponseDeletePostDTo result = postService.delete(postId, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
