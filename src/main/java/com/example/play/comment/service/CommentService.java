package com.example.play.comment.service;

import com.example.play.comment.dto.RequestCommentDto;
import com.example.play.comment.dto.ResponseCommentDto;

public interface CommentService {
    ResponseCommentDto create(RequestCommentDto commentDto, String email);

}
