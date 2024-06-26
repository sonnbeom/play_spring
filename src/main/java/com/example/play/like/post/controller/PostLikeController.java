package com.example.play.like.post.controller;

import com.example.play.jwt.dto.CustomUserDetails;
import com.example.play.like.post.dto.RequestLike;
import com.example.play.like.post.dto.ResponsePostLikeDto;

import com.example.play.like.post.service.PostLikeService;
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

    @PostMapping()
    public ResponseEntity<ResponsePostLikeDto> createLike(@RequestBody RequestLike likeRequest,
                                                          @AuthenticationPrincipal CustomUserDetails userDetails){
        ResponsePostLikeDto like = postLikeService.createLike(likeRequest ,userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.OK).body(like);
    }
}
