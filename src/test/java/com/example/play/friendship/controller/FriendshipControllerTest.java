package com.example.play.friendship.controller;

import com.example.play.friendship.constant.EndPointUrl;
import com.example.play.friendship.constant.FriendshipStatus;
import com.example.play.friendship.dto.RequestFriendship;
import com.example.play.friendship.dto.ResponseFriendshipDto;
import com.example.play.friendship.dto.ResponseFriendshipWithImg;
import com.example.play.friendship.service.FriendshipService;
import com.example.play.image.dto.ResponseMemberImg;
import com.example.play.member.dto.ResponseMemberDto;
import com.example.play.mock.WithCustomMockUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
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

import java.util.ArrayList;
import java.util.List;

import static com.example.play.friendship.constant.EndPointUrl.*;
import static com.example.play.friendship.constant.FriendshipStatus.WAITING;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@WebMvcTest(FriendshipController.class)
class FriendshipControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    WebApplicationContext context;
    @MockBean
    private FriendshipService friendshipService;
    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }
    private ObjectMapper objectMapper = new ObjectMapper();
    @Test
    @WithCustomMockUser
    @DisplayName("친구 컨트롤러: 친구 요청 생성되는지 테스트")
    void testCreateFriendship() throws Exception {
        //given
        RequestFriendship requestFriendship = new RequestFriendship("receiver@email.com");
        ResponseFriendshipDto result = ResponseFriendshipDto.builder().
                friendshipId(1L).receiverDto(new ResponseMemberDto()).received(false).senderDto(new ResponseMemberDto()).status(WAITING).build();
        when(friendshipService.create(Mockito.any(RequestFriendship.class), Mockito.anyString())).thenReturn(result);

        //when && then
        mockMvc.perform(MockMvcRequestBuilders.post(TEST_CREATE_FRIENDSHIP_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestFriendship))
                .with(csrf()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.received").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("WAITING"));

    }
    @Test
    @DisplayName("친구 컨트롤러: 대기중인 친구요청 리스트 불러오는지 테스트")
    @WithCustomMockUser
    void testGetWaitingFriendship() throws Exception {
        //given
        List<ResponseFriendshipWithImg> result = new ArrayList<>();
        ResponseFriendshipDto dto = ResponseFriendshipDto.builder().status(WAITING).build();
        ResponseMemberImg img = new ResponseMemberImg();
        ResponseFriendshipWithImg responseFriendshipWithImg = ResponseFriendshipWithImg.builder().friendshipDto(dto).img(img).build();
        result.add(responseFriendshipWithImg);
        when(friendshipService.getWaitingFriendList(Mockito.anyString(), Mockito.anyInt())).thenReturn(result);

        //when && then
        mockMvc.perform(MockMvcRequestBuilders.get(TEST_GET_WAITING_FRIENDSHIP_URL)
                .param("page", "0")
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].friendshipDto.status").value("WAITING"));


    }

}
