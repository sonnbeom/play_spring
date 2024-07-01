package com.example.play.like.controller;

import com.example.play.jwt.dto.CustomUserDetails;
import com.example.play.like.post.controller.PostLikeController;
import com.example.play.like.post.dto.RequestLike;
import com.example.play.like.post.dto.ResponseLike;
import com.example.play.like.post.dto.ResponsePostLikeDto;
import com.example.play.like.post.service.PostLikeServiceImpl;
import com.example.play.mock.WithCustomMockUser;
import com.example.play.post.dto.ResponsePostOne;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static com.example.play.like.constant.EndPointUrl.*;
import static com.example.play.like.post.dto.ResponsePostLikeDto.LikeStatus.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@WebMvcTest(PostLikeController.class)
public class PostLikeControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;
    @MockBean
    private PostLikeServiceImpl postLikeService;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }
    @DisplayName("게시글 좋아요 컨트롤러: 좋아요 생성 테스트")
    @Test
    @WithCustomMockUser
    void testPostLikeCreate() throws Exception {
        //given
        RequestLike requestLike = new RequestLike(1L);
        ResponsePostOne responsePostOne = ResponsePostOne.builder()
                .id(1L)
                .hit(1)
                .likeCount(1)
                .content("content")
                .responseImgList(new ArrayList<>())
                .title("title")
                .createdAt(LocalDateTime.now())
                .build();
        ResponsePostLikeDto responsePostLikeDto = new ResponsePostLikeDto(new ResponseLike(1L, 1),responsePostOne, CREATE);
        String userName = "test@email.com";
        CustomUserDetails customUserDetails = mock(CustomUserDetails.class);
        when(customUserDetails.getUsername()).thenReturn(userName);
        when(postLikeService.createLike(any(RequestLike.class), anyString())).thenReturn(responsePostLikeDto);
        //when && then
        mockMvc.perform(MockMvcRequestBuilders.post(TEST_POSTLIKE_CREATE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestLike))
                .with(csrf()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseLike.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseLike.isActive").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responsePostOne.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.likeStatus").value("CREATE"));

    }
}
