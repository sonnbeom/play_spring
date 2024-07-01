package com.example.play.comment.service;

import com.example.play.comment.domain.Comment;
import com.example.play.comment.dto.RequestCommentCreate;
import com.example.play.comment.dto.RequestCommentDelete;
import com.example.play.comment.dto.RequestCommentUpdate;
import com.example.play.comment.dto.ResponseComment;
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
    public ResponseComment create(RequestCommentCreate commentDto, String email) {
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
    public List<ResponseComment> getComments(Long postId, int page) {
        Post post = postService.findById(postId);
        Pageable pageable = PageRequest.of(page, COMMENT_PAGE_SIZE);
        Page<Comment> commentList = customCommentRepository.getComments(post, pageable);

        Map<Long, ResponseComment> map = new HashMap<>();
        List<ResponseComment> result = new ArrayList<>();

        commentList.stream().forEach(comment -> {
            ResponseComment dto = comment.toDto();
            map.put(dto.getId(), dto);
            if (dto.getParentId() != null){
                ResponseComment responseCommentDto = map.get(dto.getParentId());
                responseCommentDto.getChildList().add(dto);
            }else {
                result.add(dto);
            }
        });
        return result;
    }

    @Override
    public ResponseComment update(RequestCommentUpdate commentUpdate, String email) {
        Member member = memberService.findByEmail(email);
        Comment comment = findById(commentUpdate.getCommentId());
        //업데이트 권한 확인
        comment.checkUpdateAuthorization(member);
        comment.update(commentUpdate);
        return comment.toDto();
    }

    @Override
    public void delete(RequestCommentDelete commentDelete, String email) {
        Member member = memberService.findByEmail(email);
        Comment comment = findById(commentDelete.getCommentId());
        comment.checkDeleteAuthorization(member);
        commentRespository.delete(comment);
    }

    private Comment findById(Long commentId){
        return commentRespository.findById(commentId).
                orElseThrow(()->new CommentNotFoundException("해당 댓글을 조회할 수 없습니다. Id: "+commentId, HttpStatus.NOT_FOUND));
    }
}
