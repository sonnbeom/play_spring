package com.example.play.comment.service;

import com.example.play.comment.domain.Comment;
import com.example.play.comment.dto.RequestCommentCreate;
import com.example.play.comment.dto.ResponseComment;
import com.example.play.comment.repository.CommentRespository;
import com.example.play.comment.repository.CustomCommentRepository;
import com.example.play.member.entity.Member;
import com.example.play.member.service.MemberService;
import com.example.play.post.entity.Post;
import com.example.play.post.service.PostService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {
    @InjectMocks
    private CommentServiceImpl commentService;
    @Mock
    private CustomCommentRepository customCommentRepository;
    @Mock
    private CommentRespository commentRespository;
    @Mock
    private MemberService memberService;
    @Mock
    private PostService postService;
    @Test
    @DisplayName("댓글 서비스: 대댓글 생성 테스트(부모 댓글 존재)")
    void testCreateComment(){
        //given
        Member member = getTestMember();
        Post post = getTestPostWithMember(member);
        Comment parentComment = getParentComment(post);
        Comment comment = getComment(member, post, parentComment);
        RequestCommentCreate req = RequestCommentCreate.builder().content("child content").postId(1L).parentId(1L).build();
        when(postService.findById(anyLong())).thenReturn(post);
        when(memberService.findByEmail(anyString())).thenReturn(member);
        when(commentRespository.findById(anyLong())).thenReturn(Optional.ofNullable(parentComment));
        when(commentRespository.save(any(Comment.class))).thenReturn(comment);

        //when
        ResponseComment res = commentService.create(req, "test@email.com");

        //then
        assertEquals(res.getId(), 2L);
        assertEquals(res.getParentId(), 1L);
        assertEquals(res.getContent(), "child content");
        verify(postService).findById(anyLong());
        verify(memberService).findByEmail(anyString());
        verify(commentRespository).findById(anyLong());
        verify(commentRespository).save(any(Comment.class));
    }
    @Test
    @DisplayName("댓글 서비스: 댓글 생성 테스트(부모 댓글 존재 x)")
    void testCreateCommentWithOutParent(){
        //given

        //when

        //then
    }
    private Comment getParentComment(Post post){
        return Comment.builder()
                .id(1L)
                .post(post)
                .content("parent content")
                .build();
    }
    private Comment getComment(Member member,Post post, Comment parent){
        return Comment.builder()
                .id(2L)
                .post(post)
                .content("child content")
                .member(member)
                .parent(parent)
                .build();
    }

    private Member getTestMember(){
        return Member.builder()
                .email("test@email.com")
                .name("testName")
                .nickname("testNickname")
                .isActive(1)
                .password("testPwd")
                .build();
    }
    private Post getTestPostWithMember(Member member){
        return Post.builder()
                .id(1L)
                .hit(1)
                .content("content")
                .title("title")
                .isActive(1)
                .member(member)
                .build();

    }
}
