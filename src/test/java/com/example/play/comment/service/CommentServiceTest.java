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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
        Comment parentComment = getParentComment(post, member);
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
        Member member = getTestMember();
        Post post = getTestPostWithMember(member);
        Comment comment = getComment(member, post);
        RequestCommentCreate req = RequestCommentCreate.builder().content("content").postId(1L).build();
        when(postService.findById(anyLong())).thenReturn(post);
        when(memberService.findByEmail(anyString())).thenReturn(member);
        when(commentRespository.save(any(Comment.class))).thenReturn(comment);

        //when
        ResponseComment res = commentService.create(req, "test@email.com");

        //then
        assertEquals(res.getId(), 1L);
        assertEquals(res.getParentId(), null);
        assertEquals(res.getContent(), "content");
        verify(postService).findById(anyLong());
        verify(memberService).findByEmail(anyString());
        verify(commentRespository).save(any(Comment.class));
    }
    @Test
    @DisplayName("댓글 서비스: 해당 게시물과 관련된 댓글 가져오기 테스트")
    void testGetComments(){
        //given
        Member member = getTestMember();
        Post post = getTestPostWithMember(member);
        List<Comment> commentList = getComments(member, post);
        //계층 구조를 테스트 하기 위함
        Comment parentComment = getParentComment(post, member);
        Comment child = getComment(member, post, parentComment);
        commentList.add(parentComment); commentList.add(child);
        Pageable pageable = PageRequest.of(0,10);
        Page<Comment> commentPage = new PageImpl<>(commentList, pageable, 10);

        when(postService.findById(anyLong())).thenReturn(post);
        when(customCommentRepository.getComments(any(Post.class), any(Pageable.class))).thenReturn(commentPage);

        //when
        List<ResponseComment> result = commentService.getComments(1L, 0);

        //then
        //대댓글이 댓글 안에 리스트로 존재 대댓글 제외 댓글은 3개
        assertEquals(result.size(), 3);
        // 부모 댓글에 자식 댓글 하나
        assertEquals(result.get(2).getChildList().size(), 1);
        assertEquals(result.get(2).getContent(), "parent content");
        assertEquals(result.get(2).getChildList().get(0).getContent(), "child content");
        verify(postService).findById(anyLong());
        verify(customCommentRepository).getComments(any(Post.class), any(Pageable.class));
    }
    @Test
    @DisplayName("댓글 서비스: 댓글 업데이트 테스트")
    void testUpdateComment(){
        //given

        //when

        //then
    }
    private List<Comment> getComments(Member member, Post post){
        Comment comment1 = Comment.builder().member(member).content("first comment").post(post).build();
        Comment comment2 = Comment.builder().member(member).content("second comment").post(post).build();
        List<Comment> list = new ArrayList<>();
        list.add(comment1); list.add(comment2);
        return list;
    }
    private Comment getParentComment(Post post, Member member){
        return Comment.builder()
                .id(1L)
                .post(post)
                .content("parent content")
                .member(member)
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
    private Comment getComment(Member member,Post post){
        return Comment.builder()
                .id(1L)
                .post(post)
                .content("content")
                .member(member)
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
