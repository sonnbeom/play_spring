package com.example.play.comment.service;

import com.example.play.comment.dto.RequestCommentDto;
import com.example.play.comment.dto.ResponseCommentDto;

import java.util.List;

public interface CommentService {
    ResponseCommentDto create(RequestCommentDto commentDto, String email);

    List<ResponseCommentDto> getComments(Long postId, int page);
}
