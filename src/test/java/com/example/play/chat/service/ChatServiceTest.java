package com.example.play.chat.service;

import com.example.play.chat.domain.ChatMessage;
import com.example.play.chat.dto.ChatMessageResponseDto;
import com.example.play.chat.repository.ChatMessageRepository;
import com.example.play.chat.repository.CustomChatMessageRepository;
import com.example.play.chatroom.domain.ChatRoom;
import com.example.play.chatroom.service.ChatRoomService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ChatServiceTest {
    @InjectMocks
    private ChatMessageServiceImpl chatMessageService;
    @Mock
    private ChatMessageRepository chatMessageRepository;
    @Mock
    private CustomChatMessageRepository customChatMessageRepository;
    @Mock
    private ChatRoomService chatRoomService;

    @Test
    @DisplayName("채팅 서비스: 채팅방 아이디에 매핑되는 채팅 리스트 가져오는지 테스트")
    void testGetChatList(){
        //given
        List<ChatMessage> chatMesageList = getChatMesageList();
        PageRequest pageRequest = PageRequest.of(0,3);
        Page<ChatMessage> chatMessagePage= new PageImpl<>(chatMesageList, pageRequest, 3);
        when(customChatMessageRepository.getChats(any(Pageable.class), anyLong())).thenReturn(chatMessagePage);

        //when
        List<ChatMessageResponseDto> result = chatMessageService.getChats(anyInt(), anyLong());

        //then
        assertEquals(result.size(), 3);
        assertEquals(result.get(0).getMsg(), "msg1");
        assertEquals(result.get(1).getMsg(), "msg2");
        verify(customChatMessageRepository).getChats(any(Pageable.class), anyLong());
    }
    private List<ChatMessage> getChatMesageList(){
        ChatRoom chatRoom = ChatRoom.builder().build();
        ChatMessage chatMessage1 = ChatMessage.builder().chatRoom(chatRoom).msg("msg1").nickname("nickname").build();
        ChatMessage chatMessage2 = ChatMessage.builder().chatRoom(chatRoom).msg("msg2").nickname("nickname").build();
        ChatMessage chatMessage3 = ChatMessage.builder().chatRoom(chatRoom).msg("msg3").nickname("nickname").build();
        List<ChatMessage> chatMessage = Arrays.asList(chatMessage1, chatMessage2, chatMessage3);
        return chatMessage;
    }
}
