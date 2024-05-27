package com.example.play.comment.controller;

import com.example.play.comment.constant.EndPointUrl;
import com.example.play.comment.dto.RequestCommentCreate;
import com.example.play.comment.dto.ResponseComment;
import com.example.play.comment.service.CommentService;
import com.example.play.mock.WithCustomMockUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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

import static com.example.play.comment.constant.EndPointUrl.TEST_COMMENT_CREATE_URL;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CommentController.class)
public class CommentControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;
    private ObjectMapper objectMapper = new ObjectMapper();
    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }
    @MockBean
    private CommentService commentService;
    @Test
    @DisplayName("댓글 컨트롤러: 댓글 생성 테스트")
    @WithCustomMockUser
    void testCreateComment() throws Exception {
        //given
        ResponseComment responseComment = ResponseComment.builder().id(1L).content("test content").parentId(null).nickname("test nickname").build();
        RequestCommentCreate requestCommentCreate = RequestCommentCreate.builder().postId(1L).content("test content").parentId(null).build();
        when(commentService.create(any(RequestCommentCreate.class), anyString())).thenReturn(responseComment);
        //when && then
        mockMvc.perform(post(TEST_COMMENT_CREATE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestCommentCreate))
                .with(csrf()))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.content").value("test content"))
                .andExpect(jsonPath("$.parentId").doesNotExist())
                .andExpect(jsonPath("$.nickname").value("test nickname"));
    }
}
