package com.example.play.post.service;

import com.example.play.image.dto.ResponseImg;
import com.example.play.image.service.PostImgService;
import com.example.play.member.entity.Member;
import com.example.play.member.service.MemberService;
import com.example.play.post.dto.*;
import com.example.play.post.entity.Post;
import com.example.play.post.postMapper.PostMapper;
import com.example.play.post.repository.CustomPostRepository;
import com.example.play.post.repository.PostRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {
    @InjectMocks
    private PostServiceImpl postServiceImpl;
    @Mock
    private PostMapper postMapper;
    @Mock
    private MemberService memberService;
    @Mock
    private PostRepository postRepository;
    @Mock
    private PostImgService postImgService;
    @Mock
    private CustomPostRepository customPostRepository;


    @Test
    @DisplayName("포스트 서비스: 게시글이 이미지와 성공적으로 생성되는지 테스트")
    void createPostWithImg(){
        RequestPostDto requestPostDto = RequestPostDto.builder()
                .title("title")
                .content("content")
                .build();
        Member member = getTestMember();
        Post post = getTestPost();

        List<MultipartFile> files = new ArrayList<>();
        files.add(Mockito.mock(MultipartFile.class));

        List<ResponseImg> responseImgList = new ArrayList<>();
        responseImgList.add(new ResponseImg(1L, "test url"));

        Mockito.when(memberService.findByEmail(Mockito.anyString())).thenReturn(member);
        Mockito.when(postMapper.dtoToEntity(Mockito.any(RequestPostDto.class), Mockito.any(Member.class))).thenReturn(post);
        Mockito.when(postRepository.save(Mockito.any(Post.class))).thenReturn(post);
        Mockito.when(postImgService.savePostImage(Mockito.anyList(), Mockito.any(Post.class))).thenReturn(responseImgList);

        ResponsePostOne result = postServiceImpl.create(requestPostDto, files, "test@email.com");

        assertEquals(requestPostDto.getTitle(), result.getTitle());
        assertEquals(requestPostDto.getContent(), result.getContent());
        assertEquals(result.getResponseImgList().size(), 1);
        Mockito.verify(memberService).findByEmail(Mockito.anyString());
        Mockito.verify(postMapper).dtoToEntity(requestPostDto, member);
        Mockito.verify(postRepository).save(Mockito.any(Post.class));
        Mockito.verify(postImgService).savePostImage(Mockito.anyList(), Mockito.any(Post.class));

    }
    @DisplayName("포스트 서비스: 단일 게시글을 가져오기")
    @Test
    void getPost(){
        //given
        Long postId = 1L;
        Post post = getTestPost();
        List<ResponseImg> imgList = new ArrayList<>();
        imgList.add(new ResponseImg(1L, "test url"));

        Mockito.when(postRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(post));
        Mockito.when(postImgService.readImages(Mockito.any(Post.class))).thenReturn(imgList);

         //when
        ResponsePostOne result = postServiceImpl.readOne(postId);

        //then
        Mockito.verify(postRepository).findById(Mockito.anyLong());
        Mockito.verify(postImgService).readImages(Mockito.any(Post.class));
        assertEquals(result.getResponseImgList().size(), 1);
        assertEquals(result.getId(), 1);
    }
    @DisplayName("포스트 서비스: 검색을 통해 게시글 가져오기")
    @Test
    void getPostBySearch(){
        //given
        Pageable pageable = mock(Pageable.class);
        String type = "type";
        String keywokd= "keyword";
        Page<Post> postPage = mock(Page.class);
        ResponsePostDTo responsePostDTo = new ResponsePostDTo<>();
        Mockito.when(customPostRepository.findBySearch(Mockito.any(Pageable.class), Mockito.anyString(), Mockito.anyString())).thenReturn(postPage);
        Mockito.when(postMapper.pageEntityToDto(Mockito.any(Page.class))).thenReturn(responsePostDTo);

        //when
        postServiceImpl.readBySearch(1, type, keywokd);
        //then
        Mockito.verify(customPostRepository).findBySearch(Mockito.any(Pageable.class), Mockito.anyString(), Mockito.anyString());
        Mockito.verify(postMapper).pageEntityToDto(Mockito.any(Page.class));
    }
    @DisplayName("서비스 레이어에서 게시글이 성공적으로 업데이트되는지 테스트")
    @Test
    void testPostUpdate(){
        //given
        RequestUpdatePostDto updatePostDto = new RequestUpdatePostDto("update title", "update content");
        List<MultipartFile> files = new ArrayList<>();
        files.add(Mockito.mock(MultipartFile.class));
        List<Long> deleteImgList = new ArrayList<>();
        deleteImgList.add(1L);
        Member testMember = getTestMember();
        Post testPost = getTestPostWithMember(testMember);
        List<ResponseImg> imgList = Collections.singletonList(new ResponseImg(1L, "url"));


        Mockito.when(postRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(testPost));
        Mockito.when(memberService.findByEmail(Mockito.anyString())).thenReturn(testMember);
        Mockito.when(postImgService.update(Mockito.any(Post.class), Mockito.anyList(), Mockito.anyList())).thenReturn(imgList);

        //when
        ResponsePostOne result =postServiceImpl.update(1L, updatePostDto, files, deleteImgList, "test@email.com");

        //then
        assertEquals(result.getTitle(), updatePostDto.getTitle());
        assertEquals(result.getContent(), updatePostDto.getContent());
        assertEquals(result.getResponseImgList().get(0).getUrl(), imgList.get(0).getUrl());

        Mockito.verify(postRepository).findById(Mockito.anyLong());
        Mockito.verify(memberService).findByEmail(Mockito.anyString());
        Mockito.verify(postImgService).update(Mockito.any(Post.class), Mockito.anyList(), Mockito.anyList());
    }
    @DisplayName("포스트 서비스: 게시글이 성공적으로 삭제되는지 테스트")
    @Test
    void testPostDelete(){
        //given
        Member testMember = getTestMember();
        Post testPost = getTestPostWithMember(testMember);

        Mockito.when(postRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(testPost));
        Mockito.when(memberService.findByEmail(Mockito.anyString())).thenReturn(testMember);
        Mockito.doNothing().when(postImgService).deleteImg(Mockito.any(Post.class));

        //when
        ResponseDeletePostDTo result = postServiceImpl.delete(1L, "test@email.com");
        //then
        assertEquals(result.getIsActive(), 0);
        assertEquals(result.getId(), 1L);
        Mockito.verify(postRepository).findById(Mockito.anyLong());
        Mockito.verify(postImgService).deleteImg(Mockito.any(Post.class));

    }
    @Test
    @DisplayName("포스트 서비스 좋아요 한 게시글을 페이징처리하여 가져오는지 테스트")
    void getLikedPosts(){
        //given
        Member testMember = getTestMember();
        Page<Post> postPage = mock(Page.class);
        ResponsePostDTo responsePostDTo = mock(ResponsePostDTo.class);
        int currentPage = 0;

        Mockito.when(memberService.findByEmail(Mockito.anyString())).thenReturn(testMember);
        Mockito.when(customPostRepository.findLikedPosts(Mockito.any(Member.class), Mockito.any(Pageable.class))).thenReturn(postPage);
        Mockito.when(postMapper.pageEntityToDto(Mockito.any(Page.class))).thenReturn(responsePostDTo);
        //when
        ResponsePostDTo result = postServiceImpl.getLikedPosts("tese@email.com", currentPage);
        //then
        assertEquals(result.getCurrentPage(), currentPage);
        Mockito.verify(memberService).findByEmail(Mockito.anyString());
        Mockito.verify(customPostRepository).findLikedPosts(Mockito.any(Member.class), Mockito.any(Pageable.class));
        Mockito.verify(postMapper).pageEntityToDto(Mockito.any(Page.class));

    }
    // 검색, 정렬, 단일 게시글 get => test 예정
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
    private Post getTestPost(){
        return Post.builder()
                .id(1L)
                .hit(1)
                .content("content")
                .title("title")
                .isActive(1)
                .build();
    }

}
