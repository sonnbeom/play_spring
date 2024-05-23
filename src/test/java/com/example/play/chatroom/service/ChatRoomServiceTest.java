package com.example.play.chatroom.service;

import com.example.play.chat.domain.ChatRoom;
import com.example.play.chat.dto.ChatMessageResponseDto;
import com.example.play.chat.dto.ChatRoomWithMessageDto;
import com.example.play.chat.dto.RequestChatRoomDto;
import com.example.play.chat.repository.ChatRoomRepository;
import com.example.play.chat.repository.CustomChatRoomRepository;
import com.example.play.chat.service.ChatMessageService;
import com.example.play.chat.service.ChatRoomServiceImpl;
import com.example.play.member.entity.Member;
import com.example.play.member.service.MemberService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
}
