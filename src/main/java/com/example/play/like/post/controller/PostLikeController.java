package com.example.play.like.post.controller;

import com.example.play.jwt.dto.CustomUserDetails;
import com.example.play.like.post.dto.RequestLike;
import com.example.play.like.post.dto.ResponsePostLikeDto;

import com.example.play.like.post.service.PostLikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/like")
@Slf4j
public class PostLikeController {
    private final PostLikeService postLikeService;

    @Operation(summary = "게시글 좋아요 생성 혹은 삭제", description = "좋아요가 존재한다면 삭제, 없다면 생성하는 API입니다.")
    @ApiResponse(responseCode = "200", description = "좋아요 생성 및 삭제에 성공했습니다.", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "404", description = "해당 멤버를 조회할 수 없습니다..", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "404", description = "해당 게시글을 조회할 수 없습니다.", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "500", description = "해당 멤버가 누른 좋아요가 1개가 아닙니다.", content = @Content(mediaType = "application/json"))
    @PostMapping()
    public ResponseEntity<ResponsePostLikeDto> createLike(@RequestBody RequestLike likeRequest,
                                                          @AuthenticationPrincipal CustomUserDetails userDetails){
        ResponsePostLikeDto like = postLikeService.createLike(likeRequest ,userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.OK).body(like);
    }
}
