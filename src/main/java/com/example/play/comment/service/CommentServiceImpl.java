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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.play.comment.constant.CommentPageSize.COMMENT_PAGE_SIZE;


@Service
@Transactional
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{
    private final CustomCommentRepository customCommentRepository;
    private final CommentRespository commentRespository;
    private final MemberService memberService;
    private final PostService postService;

    @Override
    public ResponseCommentDto create(RequestCommentDto commentDto, String email) {
        Post post = postService.findById(commentDto.getPostId());
        Member member = memberService.findByEmail(email);
        Comment comment;
        if (commentDto.getParentId() != null){
            Comment parentComment = findById(commentDto.getParentId());
            comment = commentDto.dtoToEntityWithParent(post, member, parentComment);
        }
        else{
            comment = commentDto.dtoToEntity(post, member);
        }
        Comment saved = commentRespository.save(comment);
        return saved.toDto();
    }
    @Override
    public List<ResponseCommentDto> getComments(Long postId, int page) {
        Post post = postService.findById(postId);
        Pageable pageable = PageRequest.of(page, COMMENT_PAGE_SIZE);
        Page<Comment> commentList = customCommentRepository.getComments(post, pageable);
        Map<Long, ResponseCommentDto> map = new HashMap<>();
        List<ResponseCommentDto> result = new ArrayList<>();
        commentList.stream().forEach(comment -> {
            ResponseCommentDto dto = comment.toDto();
            map.put(dto.getId(), dto);
            if (dto.getParentId() != null){
                ResponseCommentDto responseCommentDto = map.get(dto.getParentId());
                responseCommentDto.getChildList().add(dto);
            }else {
                result.add(dto);
            }
        });
        return result;
    }
    /*
    * 패런트 아이디가 있다! => map.get => dto 에다가 해당 dto 추가
    *
    * */

    private Comment findById(Long commentId){
        return commentRespository.findById(commentId).
                orElseThrow(()->new CommentNotFoundException("해당 댓글을 조회할 수 없습니다. Id: "+commentId, HttpStatus.NOT_FOUND));
    }
}
