package com.example.play.chat.controller;

import com.example.play.chat.constant.EndPointUrl;
import com.example.play.chat.dto.ChatMessageResponseDto;
import com.example.play.chat.service.ChatMessageService;
import com.example.play.mock.WithCustomMockUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;

import static com.example.play.chat.constant.EndPointUrl.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@WebMvcTest(ChatController.class)
public class ChatControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    WebApplicationContext context;
    @MockBean
    private ChatMessageService chatMessageService;
    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }
    @DisplayName("채팅 컨트롤러: 채팅을 제대로 가져오는지 테스트")
    @Test
    @WithCustomMockUser
    void testChatGet() throws Exception {
        //given
        ChatMessageResponseDto chat1 = ChatMessageResponseDto.builder().id(1L).msg("msg1").build();
        ChatMessageResponseDto chat2 = ChatMessageResponseDto.builder().id(2L).msg("msg2").build();
        List<ChatMessageResponseDto> result = Arrays.asList(chat1, chat2);
        when(chatMessageService.getChats(anyInt(), anyLong())).thenReturn(result);
        //when && then
        mockMvc.perform(MockMvcRequestBuilders.get(TEST_CHAT_GET_URL)
                .param("page", "0")
                .param("chatRoomId", "1")
                .with(csrf()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].msg").value("msg1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].msg").value("msg2"));
    }
}
