package com.example.play.friendship.service;

import com.example.play.friendship.dto.RequestFriendship;
import com.example.play.friendship.dto.ResponseFriendshipDto;
import com.example.play.friendship.entity.Friendship;
import com.example.play.friendship.mapper.FriendshipMapper;
import com.example.play.friendship.repository.FriendshipRepository;
import com.example.play.image.service.MemberImgService;
import com.example.play.member.entity.Member;
import com.example.play.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.example.play.friendship.constant.FriendshipStatus.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FriendshipServiceTest {
    @InjectMocks
    private FriendshipServiceImpl friendshipService;
    @Mock
    private FriendshipRepository friendshipRepository;
    @Mock
    private MemberService memberService;
    @Mock
    private FriendshipMapper friendshipMapper;
    @Mock
    private MemberImgService memberImgService;

    @Test
    @DisplayName("친구 서비스: 친구 요청이 제대로 생성되는지 테스트")
    void testFriendshipCreate(){
        //given
        String receiverEmail = "receiver@email.com";
        String senderEmail = "sender@email.com";
        RequestFriendship requestFriendship = new RequestFriendship(receiverEmail);
        Member sender = getTestSender(senderEmail);
        Member receiver = getTestReceiver(receiverEmail);
        Friendship friendship = Friendship.builder().sender(sender).receiver(receiver).status(WAITING).build();
        when(memberService.findByEmail(anyString())).thenReturn(sender);
        when(memberService.findByEmail(anyString())).thenReturn(receiver);
        when(friendshipMapper.dtoToEntity(any(Member.class), any(Member.class))).thenReturn(friendship);

        //when
        ResponseFriendshipDto result = friendshipService.create(requestFriendship, "sender@email.com");

        //then
        assertEquals(result.getStatus(), WAITING);
        assertEquals(result.getReceiverDto().getEmail(), receiverEmail);
        assertEquals(result.getSenderDto().getEmail(), senderEmail);
        verify(memberService, times(2)).findByEmail(anyString());
        verify(friendshipMapper).dtoToEntity(any(Member.class), any(Member.class));
        verify(friendshipRepository).save(any(Friendship.class));
    }
    private Member getTestSender(String email){
        return Member.builder()
                .email(email)
                .name("sender")
                .nickname("senderNickname")
                .isActive(1)
                .password("senderPwd")
                .build();
    }
    private Member getTestReceiver(String email){
        return Member.builder()
                .email(email)
                .name("receiver")
                .nickname("receiverNickname")
                .isActive(1)
                .password("receiverPwd")
                .build();
    }
}
