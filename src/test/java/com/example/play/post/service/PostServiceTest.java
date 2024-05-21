package com.example.play.post.service;

import com.example.play.image.dto.ResponseImg;
import com.example.play.image.service.PostImgService;
import com.example.play.member.entity.Member;
import com.example.play.member.service.MemberService;
import com.example.play.post.dto.RequestPostDto;
import com.example.play.post.dto.ResponsePostOne;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    @DisplayName("서비스 레이어에서 게시글이 성공적으로 업데이트되는지 테스트")
    @Test
    void testPostUpdate(){

    }
    @DisplayName("서비스 레이어에서 게시글이 성공적으로 삭제되는지 테스트")
    @Test
    void testPostDelete(){

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
    private Post getTestPost(){
        return Post.builder()
                .id(1L)
                .hit(1)
                .content("content")
                .title("title")
                .isActive(1)
                .build();
    }
    private RequestPostDto getRequestPostDto(){
        return new RequestPostDto("test content", "test title");
    }
    private ResponsePostOne getTestResponsePostOne() {

        ResponseImg firstImg = new ResponseImg(1L, "first url");
        ResponseImg secondImg = new ResponseImg(1L, "first url");
        List<ResponseImg> list = new ArrayList<>();
        list.add(firstImg); list.add(secondImg);
        return ResponsePostOne.builder()
                .id(1L)
                .content("content")
                .title("title")
                .hit(1)
                .responseImgList(list)
                .likeCount(1)
                .createdAt(LocalDateTime.now())
                .build();
    }
    private MultipartFile getImgSample(){
        return new MockMultipartFile("img", "", "img.jpg", "img".getBytes());
    }

}
