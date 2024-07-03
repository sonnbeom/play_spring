package com.example.play.comment.repository;

import com.example.play.comment.domain.Comment;
import com.example.play.post.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomCommentRepository {
    Page<Comment> getComments(Post post, Pageable pageable);
}
