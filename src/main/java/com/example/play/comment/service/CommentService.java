package com.example.play.comment.service;

import com.example.play.comment.dto.RequestCommentCreate;
import com.example.play.comment.dto.RequestCommentDelete;
import com.example.play.comment.dto.RequestCommentUpdate;
import com.example.play.comment.dto.ResponseComment;

import java.util.List;

public interface CommentService {
    ResponseComment create(RequestCommentCreate commentDto, String email);

    List<ResponseComment> getComments(Long postId, int page);

    ResponseComment update(RequestCommentUpdate commentUpdate, String email);

    ResponseComment delete(RequestCommentDelete commentDelete, String email);
}
