package com.example.play.like.service;

import com.example.play.like.post.dto.RequestLike;
import com.example.play.like.post.dto.ResponsePostLikeDto;
import com.example.play.like.post.entity.PostLike;
import com.example.play.like.post.mapper.PostLikeMapper;
import com.example.play.like.post.repository.PostLikeCustomRepository;
import com.example.play.like.post.repository.PostLikeRepository;
import com.example.play.like.post.service.PostLikeServiceImpl;
import com.example.play.member.entity.Member;
import com.example.play.member.service.MemberService;
import com.example.play.member.service.MemberServiceImpl;
import com.example.play.post.entity.Post;
import com.example.play.post.service.PostService;
import com.example.play.post.service.PostServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static com.example.play.like.post.dto.ResponsePostLikeDto.LikeStatus.CREATE;
import static com.example.play.like.post.dto.ResponsePostLikeDto.LikeStatus.DELETE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PostLikeServiceTest {

    @InjectMocks
    private PostLikeServiceImpl postLikeService;
    @Mock
    private PostService postService;
    @Mock
    private MemberService memberService;
    @Mock
    private PostLikeMapper postLikeMapper;
    @Mock
    private PostLikeCustomRepository postLikeCustomRepository;
    @Mock
    private PostLikeRepository postLikeRepository;
    @Test
    @DisplayName("게시글 좋아요 서비스: 좋아요 생성")
    void createPostLike(){
        //given
        int likeCount = 0;
        Post testPost = getTestPostForCreateLike(likeCount);
        Member testMember = getTestMember();
        RequestLike requestLike = new RequestLike(1L);
        PostLike postLike = PostLike.builder().id(1L).post(testPost).isActive(1).member(testMember).build();

        when(postService.findById(Mockito.anyLong())).thenReturn(testPost);
        when(memberService.findByEmail(Mockito.anyString())).thenReturn(testMember);
        when(postLikeCustomRepository.findByPostAndMember(Mockito.any(Post.class), Mockito.any(Member.class))).thenReturn(null);
        when(postLikeMapper.createLike(Mockito.any(Post.class), Mockito.any(Member.class))).thenReturn(postLike);
        when(postLikeRepository.save(Mockito.any(PostLike.class))).thenReturn(postLike);

        //when
        ResponsePostLikeDto result = postLikeService.createLike(requestLike, "test@email.com");

        //then
        verify(postService).findById(Mockito.anyLong());
        verify(memberService).findByEmail(Mockito.anyString());
        verify(postLikeCustomRepository).findByPostAndMember(Mockito.any(Post.class), Mockito.any(Member.class));
        verify(postLikeMapper).createLike(Mockito.any(Post.class), Mockito.any(Member.class));
        assertEquals(result.getLikeStatus(), CREATE);
        assertEquals(result.getResponseLike().getIsActive(), 1);
        assertEquals(result.getResponsePostOne().getLikeCount(), likeCount+1);


    }
    @Test
    @DisplayName("게시글 좋아요 서비스: 좋아요 취소")
    void deletePostLike(){
        //given
        int likeCount = 1;
        Post testPost = getTestPostForDeleteLike(likeCount);
        Member testMember = getTestMember();
        RequestLike requestLike = new RequestLike(1L);
        PostLike postLike = PostLike.builder().id(1L).post(testPost).isActive(1).member(testMember).build();
        List<PostLike> postLikeList = new ArrayList<>();
        postLikeList.add(postLike);
        when(postService.findById(Mockito.anyLong())).thenReturn(testPost);
        when(memberService.findByEmail(Mockito.anyString())).thenReturn(testMember);
        when(postLikeCustomRepository.findByPostAndMember(Mockito.any(Post.class), Mockito.any(Member.class))).thenReturn(postLikeList);
        //when
        ResponsePostLikeDto result = postLikeService.createLike(requestLike, "test@email.com");
        //then
        verify(postService).findById(Mockito.anyLong());
        verify(memberService).findByEmail(Mockito.anyString());
        verify(postLikeCustomRepository).findByPostAndMember(Mockito.any(Post.class), Mockito.any(Member.class));
        assertEquals(result.getLikeStatus(), DELETE);
        assertEquals(result.getResponseLike().getIsActive(), 0);
        assertEquals(result.getResponsePostOne().getLikeCount(), likeCount-1);
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
    private Post getTestPostForCreateLike(int likeCount){
        return Post.builder()
                .id(1L)
                .hit(1)
                .content("content")
                .likeCount(likeCount)
                .title("title")
                .isActive(1)
                .build();
    }
    private Post getTestPostForDeleteLike(int likeCount){
        return Post.builder()
                .id(1L)
                .hit(1)
                .content("content")
                .likeCount(likeCount)
                .title("title")
                .isActive(1)
                .build();
    }

}
