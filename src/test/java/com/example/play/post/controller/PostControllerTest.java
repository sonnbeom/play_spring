package com.example.play.post.controller;

import com.example.play.mock.WithCustomMockUser;
import com.example.play.post.dto.ResponsePostOne;
import com.example.play.post.service.PostServiceImpl;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.regex.Matcher;

import static com.example.play.post.constant.EndPointUrl.TEST_POST_GET_ONE_URL;
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

    @DisplayName("게시글이 성공적으로 생성되는지 테스트")
    @Test
    void postCreateTest(){

    }
    @DisplayName("단일 게시글을 성공적으로 가져오는지 테스트")
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
    @DisplayName("사용자가 좋아요한 게시글을 성공적으로 가져오는지 테스트")
    @Test
    void getLikedPosts(){

    }
    @DisplayName("검색 조건에 맞는 게시글을 성공적으로 검색하는지 테스트")
    @Test
    void getPostsBySearch(){

    }
    @DisplayName("특정 조건으로 정렬된 게시글을 성공적으로 가져오는지 테스트")
    @Test
    void getPostsSorted(){

    }

}
