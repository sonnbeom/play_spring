package com.example.play.comment.repository;

import com.example.play.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRespository extends JpaRepository<Comment, Long> {
}
