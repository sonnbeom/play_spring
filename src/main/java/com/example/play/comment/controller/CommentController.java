package com.example.play.comment.controller;

import com.example.play.comment.dto.RequestCommentCreate;
import com.example.play.comment.dto.RequestCommentDelete;
import com.example.play.comment.dto.RequestCommentUpdate;
import com.example.play.comment.dto.ResponseComment;
import com.example.play.comment.service.CommentService;
import com.example.play.jwt.dto.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "댓글 생성", description = "게시글에 댓글 생성하는 API입니다.")
    @PostMapping
    public ResponseEntity<ResponseComment>create(@RequestBody RequestCommentCreate commentDto,
                                                 @AuthenticationPrincipal CustomUserDetails customUserDetails){
        ResponseComment result = commentService.create(commentDto, customUserDetails.getUsername());
        return ResponseEntity.status(CREATED).body(result);
    }
    @Operation(summary = "댓글 조회", description = "게시글에 해당되는 댓글을 조회하는 API입니다.")
    @GetMapping
    public ResponseEntity<List<ResponseComment>>getComments(@RequestParam Long postId,
                                                            @RequestParam(required = false, defaultValue = "0") int page){
        List<ResponseComment> result = commentService.getComments(postId, page);
        return ResponseEntity.status(OK).body(result);
    }
    @Operation(summary = "댓글 수정", description = "댓글을 수정하는 API입니다.")
    @PatchMapping
    public ResponseEntity<ResponseComment>update(@RequestBody RequestCommentUpdate commentUpdate,
                                                 @AuthenticationPrincipal CustomUserDetails customUserDetails){
        ResponseComment result = commentService.update(commentUpdate, customUserDetails.getUsername());
        return ResponseEntity.status(OK).body(result);
    }
    @Operation(summary = "댓글 삭제", description = "댓글을 삭제하는 API입니다.")
    @DeleteMapping
    public ResponseEntity<Void>delete(@RequestBody RequestCommentDelete commentDelete,
                                                 @AuthenticationPrincipal CustomUserDetails customUserDetails){
        commentService.delete(commentDelete, customUserDetails.getUsername());
        return ResponseEntity.status(OK).build();
    }
}
