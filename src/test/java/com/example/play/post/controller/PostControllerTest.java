package com.example.play.post.controller;

import com.example.play.image.dto.ResponseImg;
import com.example.play.mock.WithCustomMockUser;
import com.example.play.post.dto.*;
import com.example.play.post.service.PostServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


import static com.example.play.post.constant.EndPointUrl.*;
import static com.example.play.post.dto.ResponseDeletePostDTo.DeleteStatus.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@WebMvcTest(PostController.class)
class PostControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PostServiceImpl postServiceImpl;
    @Autowired
    private WebApplicationContext context;
    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }
    private ObjectMapper objectMapper = new ObjectMapper();

    @DisplayName("게시글 컨트롤러: 게시글이 성공적으로 생성되는지 테스트")
    @Test
    @WithCustomMockUser
    void postCreateTest() throws Exception {
        //when

        // 이미지
        MockMultipartFile fileOne = new MockMultipartFile("files", "files.jpg", "image/jpg", "image.jpg".getBytes());
        MockMultipartFile fileTwo = new MockMultipartFile("files", "files.jpg", "image/jpeg", "image.jpeg".getBytes());
        // 게시글
        RequestPostDto requestPostDto = new RequestPostDto("test tile", "test content");
        MockMultipartFile mockPostDto = new MockMultipartFile("postDto", "postDto", "application/json", objectMapper.writeValueAsBytes(requestPostDto));
        // 결과
        LocalDateTime time = LocalDateTime.now();

        List<ResponseImg> listImg = new ArrayList<>();
        ResponseImg firstImg = new ResponseImg(1L, "test/path/1");
        ResponseImg secondImg = new ResponseImg(2L, "test/path/2");
        listImg.add(firstImg); listImg.add(secondImg);

        ResponsePostOne responsePostOne = new ResponsePostOne(1L, "test title", "test content", 1, 1, listImg, time);

        Mockito.when(postServiceImpl.create(Mockito.any(RequestPostDto.class), Mockito.anyList(), Mockito.anyString())).thenReturn(responsePostOne);

        //given && then
        mockMvc.perform(MockMvcRequestBuilders.multipart(TEST_POST_CREATE_URL)
                .file(fileOne)
                .file(fileTwo)
                .file(mockPostDto)
                .with(csrf()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("test title"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value("test content"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.hit").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.likeCount").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseImgList[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseImgList[0].url").value("test/path/1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseImgList[1].id").value(2L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseImgList[1].url").value("test/path/2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.createdAt").value(time.format(DateTimeFormatter.ISO_DATE_TIME)));
    }
    @DisplayName("게시글 컨트롤러: 단일 게시글을 성공적으로 가져오는지 테스트")
    @Test
    @WithCustomMockUser
    void getPostTest() throws Exception {
        //when
        Long postId = 1L;
        LocalDateTime time = LocalDateTime.now();
        ResponsePostOne responsePostOne = new ResponsePostOne(1L, "test title", "test content", 1, 1, new ArrayList<>(), time);
        Mockito.when(postServiceImpl.readOne(postId)).thenReturn(responsePostOne);

        // when && then
        mockMvc.perform(MockMvcRequestBuilders.get(TEST_POST_GET_ONE_URL, 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("test title"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value("test content"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.hit").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.likeCount").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseImgList").value(Matchers.empty()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.createdAt").value(time.format(DateTimeFormatter.ISO_DATE_TIME)));

    }
    @DisplayName("게시글 컨트롤러: 검색 조건에 맞는 게시글을 성공적으로 검색하는지 테스트")
    @Test
    @WithCustomMockUser
    void getPostsBySearch() throws Exception {
        //given
        ResponsePostPageDto postDto_1 = new ResponsePostPageDto(1L, "first title", 1, 1, "first url");
        ResponsePostPageDto postDto_2 = new ResponsePostPageDto(2L, "second title", 2, 2, "second url");
        List<ResponsePostPageDto> list = new ArrayList<>();
        list.add(postDto_1); list.add(postDto_2);
        ResponsePostDTo responsePostDTo = new ResponsePostDTo<>(0, 1, list);
        Mockito.when(postServiceImpl.readBySort(Mockito.anyInt(), Mockito.anyString())).thenReturn(responsePostDTo);

        //when && then
        mockMvc.perform(MockMvcRequestBuilders.get(TEST_POST_SORT_URL)
                .param("page", String.valueOf(0))
                .param("sortType", "sortType")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.currentPage").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalPages").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.postListDto[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.postListDto[0].title").value("first title"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.postListDto[0].likeCount").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.postListDto[0].url").value("first url"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.postListDto[1].id").value(2L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.postListDto[1].title").value("second title"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.postListDto[1].likeCount").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.postListDto[1].url").value("second url"));
    }
    @DisplayName("게시글 컨트롤러: 정렬 조건에 맞는 게시글을 성공적으로 검색하는지 테스트")
    @Test
    @WithCustomMockUser
    void getPostsBySort() throws Exception {
        //given
        ResponsePostPageDto postDto_1 = new ResponsePostPageDto(1L, "first title", 1, 1, "first url");
        ResponsePostPageDto postDto_2 = new ResponsePostPageDto(2L, "second title", 2, 2, "second url");
        List<ResponsePostPageDto> list = new ArrayList<>();
        list.add(postDto_1); list.add(postDto_2);
        ResponsePostDTo responsePostDTo = new ResponsePostDTo<>(0, 1, list);
        Mockito.when(postServiceImpl.readBySearch(Mockito.anyInt(), Mockito.anyString(), Mockito.anyString())).thenReturn(responsePostDTo);
        //when && then
        mockMvc.perform(MockMvcRequestBuilders.get(TEST_POST_SEARCH_URL)
                .param("page", String.valueOf(0))
                .param("type", "type")
                .param("keyword", "keyword")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.currentPage").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalPages").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.postListDto[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.postListDto[0].title").value("first title"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.postListDto[0].likeCount").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.postListDto[0].url").value("first url"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.postListDto[1].id").value(2L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.postListDto[1].title").value("second title"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.postListDto[1].likeCount").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.postListDto[1].url").value("second url"));
    }
    @DisplayName("게시글 컨트롤러: 좋아요한 게시글을 성공적으로 검색하는지 테스트")
    @Test
    @WithCustomMockUser
    void getLikedPosts() throws Exception {
        //given
        ResponsePostPageDto postDto_1 = new ResponsePostPageDto(1L, "first title", 1, 1, "first url");
        ResponsePostPageDto postDto_2 = new ResponsePostPageDto(2L, "second title", 2, 2, "second url");
        List<ResponsePostPageDto> list = new ArrayList<>();
        list.add(postDto_1); list.add(postDto_2);
        ResponsePostDTo responsePostDTo = new ResponsePostDTo<>(0, 1, list);

        Mockito.when(postServiceImpl.getLikedPosts(Mockito.anyString(), Mockito.anyInt())).thenReturn(responsePostDTo);
        //when && then
        mockMvc.perform(MockMvcRequestBuilders.get(TEST_POST_GET_LIKED_POST_URL)
                .param("page", String.valueOf(0))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.currentPage").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalPages").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.postListDto[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.postListDto[0].title").value("first title"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.postListDto[0].likeCount").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.postListDto[0].url").value("first url"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.postListDto[1].id").value(2L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.postListDto[1].title").value("second title"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.postListDto[1].likeCount").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.postListDto[1].url").value("second url"));
    }
    @DisplayName("포스트 컨트롤러: 게시글을 정상적으로 수정하는지 테스트")
    @Test
    @WithCustomMockUser
    void patchPostTest() throws Exception {
        //given
        //업데이트 하는 게시글 객체
        RequestUpdatePostDto requestUpdatePostDto = new RequestUpdatePostDto("update title", "update content");
        MockMultipartFile mockUpdatePostDto = new MockMultipartFile("updatePostDto", "", "application/json", objectMapper.writeValueAsBytes(requestUpdatePostDto));
        // 이미지
        MockMultipartFile fileOne = new MockMultipartFile("files", "files.jpg", "image/jpg", "image.jpg".getBytes());
        MockMultipartFile fileTwo = new MockMultipartFile("files", "files.jpg", "image/jpeg", "image.jpeg".getBytes());

        LocalDateTime time = LocalDateTime.now();
        ResponsePostOne responsePostOne = new ResponsePostOne(1L, "update title", "update content", 1, 1, new ArrayList<>(), time);

        List<Long> deleteFileList = new ArrayList<>();
        deleteFileList.add(1L); deleteFileList.add(2L);
        MockMultipartFile deleteFileIdList = new MockMultipartFile("deleteFileList", "", "application/json", objectMapper.writeValueAsBytes(deleteFileList));
        Mockito.when(postServiceImpl.update(Mockito.anyLong(), Mockito.any(RequestUpdatePostDto.class), Mockito.anyList(), Mockito.anyList(), Mockito.anyString())).thenReturn(responsePostOne);

        //when && then
        mockMvc.perform(MockMvcRequestBuilders.multipart(HttpMethod.PATCH, TEST_POST_PATCH_URL, 1L)
                .file(fileOne)
                .file(fileTwo)
                .file(mockUpdatePostDto)
                .file(deleteFileIdList)
                .with(csrf()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("update title"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value("update content"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.hit").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.likeCount").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseImgList").value(Matchers.empty()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.createdAt").value(time.format(DateTimeFormatter.ISO_DATE_TIME)));
    }
    @DisplayName("포스트 컨트롤러: 게시글이 정상적으로 삭제되는지 테스트")
    @Test
    @WithCustomMockUser
    void testPostDelete() throws Exception {
        //given
        ResponseDeletePostDTo deletePostDTo = ResponseDeletePostDTo.builder().
                id(1L).hit(1).content("content").isActive(0).status(DELETED).title("title").likeCount(1).build();
        Mockito.when(postServiceImpl.delete(Mockito.anyLong(), Mockito.anyString())).thenReturn(deletePostDTo);
        //when && then
        mockMvc.perform(MockMvcRequestBuilders.delete(TEST_POST_DELETE_URL, 1)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.hit").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value("content"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isActive").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("DELETED"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("title"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.likeCount").value(1));



    }

}
