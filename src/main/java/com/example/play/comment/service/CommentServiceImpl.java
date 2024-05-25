package com.example.play.comment.service;

import com.example.play.comment.domain.Comment;
import com.example.play.comment.dto.RequestCommentDto;
import com.example.play.comment.dto.ResponseCommentDto;
import com.example.play.comment.exception.CommentNotFoundException;
import com.example.play.comment.repository.CommentRespository;
import com.example.play.comment.repository.CustomCommentRepository;
import com.example.play.member.entity.Member;
import com.example.play.member.service.MemberService;
import com.example.play.post.entity.Post;
import com.example.play.post.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService{
    private final CustomCommentRepository customCommentReposiotory;
    private final CommentRespository commentRespository;
    private final MemberService memberService;
    private final PostService postService;

    @Override
    public ResponseCommentDto create(RequestCommentDto commentDto, String email) {
        Post post = postService.findById(commentDto.getPostId());
        Member member = memberService.findByEmail(email);
        Comment comment;
        if (commentDto.hasParenId()){
            Comment parentComment = findById(commentDto.getParentId());
            comment = commentDto.dtoToEntityWithParent(post, member, parentComment);
        }
        else{
            comment = commentDto.dtoToEntity(post, member);
        }
        Comment saved = commentRespository.save(comment);
        return saved.toDto();
    }
    private Comment findById(Long commentId){
        return commentRespository.findById(commentId).
                orElseThrow(()->new CommentNotFoundException("해당 댓글을 조회할 수 없습니다. Id: "+commentId, HttpStatus.NOT_FOUND));
    }
}
