package com.example.play.comment.controller;

import com.example.play.comment.dto.RequestCommentDto;
import com.example.play.comment.dto.ResponseCommentDto;
import com.example.play.comment.service.CommentService;
import com.example.play.jwt.dto.CustomUserDetails;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comment")
@Slf4j
public class CommentController {
    private final CommentService commentService;
    /*
    * 1. 댓글 생성
    * 2. 댓글 조회 -> 게시글 함께 보는 경우가 있기 때문 x 일단 보류
    * 3. 댓글 수정
    * 4. 댓글 삭제
    * */
    @PostMapping()
    public ResponseEntity<ResponseCommentDto>create(@RequestBody RequestCommentDto commentDto,
                                                    @AuthenticationPrincipal CustomUserDetails customUserDetails){
        ResponseCommentDto result = commentService.create(commentDto, customUserDetails.getUsername());
        return ResponseEntity.status(CREATED).body(result);
    }
    @GetMapping
    public ResponseEntity<List<ResponseCommentDto>>getComments(@RequestParam Long postId,
                                                               @RequestParam(required = false, defaultValue = "0") int page){
        List<ResponseCommentDto> result = commentService.getComments(postId, page);
        return ResponseEntity.status(OK).body(result);
    }
}
