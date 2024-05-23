package com.example.play.chatroom.controller;

import com.example.play.chat.controller.ChatRoomController;
import com.example.play.chat.dto.*;
import com.example.play.chat.service.ChatRoomService;
import com.example.play.chatroom.constant.EndPointUrl;
import com.example.play.mock.WithCustomMockUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.example.play.chatroom.constant.EndPointUrl.TEST_CHATROOM_CREATE_URL;
import static com.example.play.chatroom.constant.EndPointUrl.TEST_CHATROOM_GET_URL;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@WebMvcTest(ChatRoomController.class)
public class ChartRoomControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    WebApplicationContext context;
    @MockBean
    private ChatRoomService chatRoomService;
    private ObjectMapper objectMapper = new ObjectMapper();
    void setUp(){
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }
    @Test
    @DisplayName("채팅방 컨트롤러: 채팅방이 있다면 존재하는 채팅방, 메시지 가져오는지 테스트")
    @WithCustomMockUser
    void testCreateChatRoom() throws Exception {
        //given
        RequestChatRoomDto requestChatRoomDto = new RequestChatRoomDto("other@email.com");
        ChatRoomDto chatRoomDto = ChatRoomDto.builder().chatRoomId(1L).nickname("nickname").otherNickname("other nickname").build();
        ChatMessageResponseDto chat1 = ChatMessageResponseDto.builder().id(1L).msg("msg1").dateTime(LocalDateTime.now()).build();
        ChatMessageResponseDto chat2 = ChatMessageResponseDto.builder().id(2L).msg("msg2").dateTime(LocalDateTime.now()).build();
        List<ChatMessageResponseDto> chatList = new ArrayList<>();
        chatList.add(chat1); chatList.add(chat2);
        ChatRoomWithMessageDto result = ChatRoomWithMessageDto.builder().chatRoomDto(chatRoomDto).chatMessage(chatList).build();
        when(chatRoomService.makeRoom(any(RequestChatRoomDto.class), anyString())).thenReturn(result);

        //when && then
        mockMvc.perform(MockMvcRequestBuilders.post(TEST_CHATROOM_CREATE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestChatRoomDto))
                .with(csrf()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.chatRoomDto.nickname").value("nickname"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.chatRoomDto.otherNickname").value("other nickname"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.chatMessage", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.chatMessage[0].msg").value("msg1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.chatMessage[1].msg").value("msg2"));
    }
    @Test
    @DisplayName("채팅방 컨트롤러: 채팅방들과 채팅방들과 상응하는 채팅을 가져오는지 테스트")
    @WithCustomMockUser
    void testGetChatRooms() throws Exception {
        //given
        List<ChatRoomsWithChatsDto> result = getChatRoomsWithChatsDto();
        when(chatRoomService.getChatRooms(anyInt(), anyString())).thenReturn(result);

        //when && then
        mockMvc.perform(MockMvcRequestBuilders.get(TEST_CHATROOM_GET_URL)
                .param("page", "0")
                .with(csrf()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].chatRoomDto.nickname").value("nickname"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].chatRoomDto.otherNickname").value("other nickname1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].chatRoomDto.nickname").value("nickname"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].chatRoomDto.otherNickname").value("other nickname2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].chatMessage.msg").value("msg1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].chatMessage.msg").value("msg2"));
    }
    private List<ChatRoomsWithChatsDto> getChatRoomsWithChatsDto(){
        ChatRoomDto chatRoomDto1 = ChatRoomDto.builder().chatRoomId(1L).nickname("nickname").otherNickname("other nickname1").build();
        ChatMessageResponseDto chat1 = ChatMessageResponseDto.builder().id(1L).msg("msg1").dateTime(LocalDateTime.now()).build();

        ChatRoomDto chatRoomDto2 = ChatRoomDto.builder().chatRoomId(2L).nickname("nickname").otherNickname("other nickname2").build();
        ChatMessageResponseDto chat2 = ChatMessageResponseDto.builder().id(2L).msg("msg2").dateTime(LocalDateTime.now()).build();

        ChatRoomsWithChatsDto chatRoomsWithChatsDto1 = ChatRoomsWithChatsDto.builder().chatRoomDto(chatRoomDto1).chatMessage(chat1).build();
        ChatRoomsWithChatsDto chatRoomsWithChatsDto2 = ChatRoomsWithChatsDto.builder().chatRoomDto(chatRoomDto2).chatMessage(chat2).build();
        List<ChatRoomsWithChatsDto> list = new ArrayList<>();
        list.add(chatRoomsWithChatsDto1); list.add(chatRoomsWithChatsDto2);
        return list;

    }
}
