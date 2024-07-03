package com.example.play.chatroom.service;

import com.example.play.chat.domain.ChatMessage;
import com.example.play.chatroom.domain.ChatRoom;
import com.example.play.chat.dto.ChatMessageResponseDto;
import com.example.play.chatroom.dto.ChatRoomWithMessageDto;
import com.example.play.chatroom.dto.ChatRoomsWithChatsDto;
import com.example.play.chatroom.dto.RequestChatRoomDto;
import com.example.play.chatroom.repository.ChatRoomRepository;
import com.example.play.chatroom.repository.CustomChatRoomRepository;
import com.example.play.chat.service.ChatMessageService;
import com.example.play.member.domain.Member;
import com.example.play.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ChatRoomServiceTest {
    @InjectMocks
    private ChatRoomServiceImpl chatRoomService;
    @Mock
    private ChatRoomRepository chatRoomRepository;
    @Mock
    private CustomChatRoomRepository customChatRoomRepository;
    @Mock
    private MemberService memberService;
    @Mock
    private ChatMessageService chatMessageService;

    @Test
    @DisplayName("채팅방 서비스: 채팅방이 있는 경우 채팅방과 채팅 불러오는지 테스트")
    void testChatRoomCreate(){
        //given
        String memberNickname = "nickname";
        String otherNickname = "otherNickname";
        Member member = getTestMember(memberNickname);
        Member other = getTestOther(otherNickname);
        RequestChatRoomDto requestChatRoomDto = new RequestChatRoomDto("other@email.com");
        ChatRoom chatRoom = ChatRoom.builder().member(member).other(other).id(1L).build();
        ChatMessageResponseDto chat1 = ChatMessageResponseDto.builder().msg("msg1").id(1L).build();
        ChatMessageResponseDto chat2 = ChatMessageResponseDto.builder().msg("msg2").id(2L).build();
        List<ChatMessageResponseDto> list = new ArrayList<>();
        list.add(chat1); list.add(chat2);
        when(memberService.findByEmail(anyString())).thenReturn(member);
        when(memberService.findByEmail(anyString())).thenReturn(other);
        when(chatRoomRepository.findByMemberAndOther(any(Member.class), any(Member.class))).thenReturn(Optional.ofNullable(chatRoom));
        when(chatMessageService.findByRoom(any(ChatRoom.class))).thenReturn(list);
        //when
        ChatRoomWithMessageDto result = chatRoomService.makeRoom(requestChatRoomDto, "member@email.com");

        //then
        assertEquals(result.getChatRoomDto().getNickname(), memberNickname);
        assertEquals(result.getChatRoomDto().getOtherNickname(), otherNickname);
        assertEquals(result.getChatMessage().size(), 2);
    }
    @Test
    @DisplayName("채팅방 서비스: 채팅방 리스트 가져오는지 테스트")
    void getChatRoomList(){
        //given
        Member testMember = getTestMember("nickname");
        //채팅방 리스트
        Page<ChatRoom> pageChatRoom = getPageChatRoom(testMember);
        //채팅 메시지 리스트
        List<ChatMessage> chatMessages = getChatMessages(pageChatRoom.getContent());
        //예상되는 채팅방 id List
        List<Long> expectedIds = Arrays.asList(1L, 2L, 3L);
        when(memberService.findByEmail(anyString())).thenReturn(testMember);
        when(customChatRoomRepository.findRooms(any(Member.class), any(Pageable.class))).thenReturn(pageChatRoom);
        when(chatMessageService.findByRoomChatRoomList(anyList())).thenReturn(chatMessages);

        //when
        List<ChatRoomsWithChatsDto> result = chatRoomService.getChatRooms(0, "member@email.com");

        //then
        List<Long> actualIds = result.stream()
                        .map(chatRoomsWithChatsDto -> chatRoomsWithChatsDto.getChatRoomDto().getChatRoomId())
                        .sorted()
                        .collect(Collectors.toList());

        assertEquals(actualIds, expectedIds);
        assertEquals(result.get(0).getChatRoomDto().getNickname(), "nickname");
        assertEquals(result.get(1).getChatRoomDto().getNickname(), "nickname");
        assertEquals(result.get(2).getChatRoomDto().getNickname(), "nickname");
        assertEquals(result.size(), 3);
        verify(memberService).findByEmail(anyString());
        verify(customChatRoomRepository).findRooms(any(Member.class), any(Pageable.class));
        verify(chatMessageService).findByRoomChatRoomList(anyList());
    }

    private List<ChatMessage> getChatMessages(List<ChatRoom> content) {
        ChatMessage chatMessage1 = ChatMessage.builder().id(1L).msg("msg1").nickname("nickname").chatRoom(content.get(0)).build();
        ChatMessage chatMessage2 = ChatMessage.builder().id(2L).msg("msg2").nickname("nickname").chatRoom(content.get(1)).build();
        ChatMessage chatMessage3 = ChatMessage.builder().id(3L).msg("msg3").nickname("nickname").chatRoom(content.get(2)).build();
        List<ChatMessage> chatMessages = Arrays.asList(chatMessage1, chatMessage2, chatMessage3);
        return chatMessages;
    }

    private Member getTestOther(String nickname){
        return Member.builder()
                .email("other@email.com")
                .name("other")
                .nickname(nickname)
                .isActive(1)
                .password("otherPwd")
                .build();
    }
    private Member getTestMember(String nickname){
        return Member.builder()
                .email("member@email.com")
                .name("member")
                .nickname(nickname)
                .isActive(1)
                .password("memberPwd")
                .build();
    }
    private Page<ChatRoom> getPageChatRoom(Member member){
        ChatRoom chatRoom1 = ChatRoom.builder().id(1L).member(member).other(getTestOther("other1")).build();
        ChatRoom chatRoom2 = ChatRoom.builder().id(2L).member(member).other(getTestOther("other2")).build();
        ChatRoom chatRoom3 = ChatRoom.builder().id(3L).member(member).other(getTestOther("other3")).build();
        List<ChatRoom> list = Arrays.asList(chatRoom1, chatRoom2, chatRoom3);
        Page<ChatRoom> chatRoomPage = new PageImpl<>(list);
        return chatRoomPage;
    }
}
