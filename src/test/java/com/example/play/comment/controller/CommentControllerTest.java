package com.example.play.comment.controller;

import com.example.play.comment.constant.EndPointUrl;
import com.example.play.comment.dto.RequestCommentCreate;
import com.example.play.comment.dto.RequestCommentDelete;
import com.example.play.comment.dto.RequestCommentUpdate;
import com.example.play.comment.dto.ResponseComment;
import com.example.play.comment.service.CommentService;
import com.example.play.mock.WithCustomMockUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
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

import java.util.Arrays;
import java.util.List;

import static com.example.play.comment.constant.EndPointUrl.*;
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
    @Test
    @DisplayName("댓글 컨트롤러: get 요청 테스트")
    @WithCustomMockUser
    void testGetCommentList() throws Exception {
        //given
        ResponseComment comment1 = ResponseComment.builder().id(1L).content("1 content").parentId(null).nickname("test nickname").build();
        ResponseComment comment2 = ResponseComment.builder().id(2L).content("2 content").parentId(null).nickname("test nickname").build();
        ResponseComment comment3 = ResponseComment.builder().id(3L).content("child").parentId(1L).nickname("child").build();
        List<ResponseComment> result = Arrays.asList(comment1, comment2, comment3);
        when(commentService.getComments(anyLong(),anyInt())).thenReturn(result);

        //when && then
        mockMvc.perform(get(TEST_COMMENT_GET_URL)
                .param("postId", "1")
                .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(3)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].content").value("1 content"))
                .andExpect(jsonPath("$[1].parentId").doesNotExist())
                .andExpect(jsonPath("$[2].content").value("child"))
                .andExpect(jsonPath("$[2].parentId").value(1));

    }
    @Test
    @DisplayName("댓글 컨트롤러: 댓글 업데이트 테스트")
    @WithCustomMockUser
    void testUpdateComment() throws Exception {
        //given
        RequestCommentUpdate req = RequestCommentUpdate.builder().commentId(1L).content("update").build();
        ResponseComment result = ResponseComment.builder().id(1L).content("update").parentId(null).nickname("test nickname").build();
        when(commentService.update(any(RequestCommentUpdate.class), anyString())).thenReturn(result);

        //when %% then
        mockMvc.perform(patch(TEST_COMMENT_PATCH_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req))
                .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.content").value("update"));

    }
    @Test
    @DisplayName("댓글 컨트롤러: 댓글 삭제 테스트")
    @WithCustomMockUser
    void testDeleteComment() throws Exception {
        //given
        RequestCommentDelete req = RequestCommentDelete.builder().commentId(1L).build();
        doNothing().when(commentService).delete(any(RequestCommentDelete.class), anyString());
        //when %% then
        mockMvc.perform(delete(TEST_COMMENT_DELETE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req))
                .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk());

    }
}
