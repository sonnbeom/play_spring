package com.example.play.friendship.service;

import com.example.play.friendship.dto.RequestDeleteFriendship;
import com.example.play.friendship.dto.RequestFriendship;
import com.example.play.friendship.dto.ResponseFriendshipDto;
import com.example.play.friendship.dto.ResponseFriendshipWithImg;
import com.example.play.friendship.entity.Friendship;
import com.example.play.friendship.mapper.FriendshipMapper;
import com.example.play.friendship.repository.FriendshipCustomRepository;
import com.example.play.friendship.repository.FriendshipRepository;
import com.example.play.image.entity.MemberImage;
import com.example.play.image.service.MemberImgService;
import com.example.play.member.entity.Member;
import com.example.play.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import java.util.*;

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
    private FriendshipCustomRepository friendshipCustomRepository;
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
        Member sender = getTestSender(senderEmail);
        Member receiver = getTestReceiver(receiverEmail);

        RequestFriendship requestFriendship = new RequestFriendship(receiverEmail);
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
    @Test
    @DisplayName("친구 서비스: 친구 요청이 제대로 수락되는지 테스트")
    void testFriendshipApprove(){
        //given
        String receiverEmail = "receiver@email.com";
        String senderEmail = "sender@email.com";
        Member sender = getTestSender(senderEmail);
        Member receiver = getTestReceiver(receiverEmail);
        Friendship friendship = Friendship.builder().sender(sender).receiver(receiver).status(WAITING).build();
        when(memberService.findByEmail(anyString())).thenReturn(receiver);
        when(friendshipRepository.findById(anyLong())).thenReturn(Optional.ofNullable(friendship));
        //when
        ResponseFriendshipDto result = friendshipService.approveFriendship(1L, receiverEmail);
        //then
        assertEquals(result.getReceiverDto().getEmail(), receiverEmail);
        assertEquals(result.getStatus(), ACCEPTED);
        verify(memberService).findByEmail(anyString());
        verify(friendshipRepository).findById(anyLong());
    }
    @Test
    @DisplayName("친구 서비스: 대기 중인 친구요청 불러오기")
    void tesGetFriendshipWaitingList(){
        //given
        // 친구요청 받은 멤버
        String receiverEmail = "receiver@email.com";
        Member receiver = getTestReceiver(receiverEmail);
        // 친구 요청 보낸 멤버 1
        String senderEmail = "sender@email.com";
        Member sender = getTestSender(senderEmail);
        // 친구 요청 보낸 멤버 2
        String secondSenderEmail = "secondSender@email.com";
        Member secondSender = getTestSender(secondSenderEmail);

        // DB 조회시 반환될 친구 리스트 (수락된 친구 리스트)
        List<Friendship> friendshipList = getWaitingFriendshipList(receiver, sender, secondSender);

        // 친구요청한 멤버들의 사진 (이미지 서비스에서 가져옴)
        List<MemberImage> friendsImgList = getFriendsImgList(sender, secondSender);

        when(memberService.findByEmail(anyString())).thenReturn(receiver);
        when(friendshipCustomRepository.findWaitinFrinedshipList(any(Member.class), any(Pageable.class))).thenReturn(friendshipList);
        when(memberImgService.findImgListByIdList(anyList())).thenReturn(friendsImgList);

        //when
        List<ResponseFriendshipWithImg> result = friendshipService.getWaitingFriendList(receiverEmail, 0);

        //then
        assertEquals(result.size(), 2);
        assertEquals(result.get(0).getFriendshipDto().getStatus(), WAITING);
        assertEquals(result.get(1).getFriendshipDto().getStatus(), WAITING);
        assertEquals(result.get(0).getFriendshipDto().getReceiverDto().getEmail(), receiverEmail);
        assertEquals(result.get(1).getFriendshipDto().getReceiverDto().getEmail(), receiverEmail);
        assertEquals(result.get(0).getImg().getUrl(), "test url");
        assertEquals(result.get(1).getImg().getUrl(), "test url2");
        verify(memberService).findByEmail(anyString());
        verify(friendshipCustomRepository).findWaitinFrinedshipList(any(Member.class), any(Pageable.class));
        verify(memberImgService).findImgListByIdList(anyList());
    }
    @Test
    @DisplayName("친구 서비스: 친구 리스트 불러는지 테스트")
    void getFriendList(){
        //given
        // 친구요청 받은 멤버
        String receiverEmail = "receiver@email.com";
        Member receiver = getTestReceiver(receiverEmail);
        // 친구 요청 보낸 멤버 1
        String senderEmail = "sender@email.com";
        Member sender = getTestSender(senderEmail);
        // 친구 요청 보낸 멤버 2
        String secondSenderEmail = "secondSender@email.com";
        Member secondSender = getTestSender(secondSenderEmail);
        // DB 조회시 반환될 친구 리스트 (수락된 친구 리스트)
        List<Friendship> friendshipList = getFriendshipList(receiver, sender, secondSender);

        // 친구요청한 멤버들의 사진 (이미지 서비스에서 가져옴)
        List<MemberImage> friendsImgList = getFriendsImgList(sender, secondSender);

        when(memberService.findByEmail(anyString())).thenReturn(receiver);
        when(friendshipCustomRepository.findFriendListByMember(any(Member.class))).thenReturn(friendshipList);
        when(memberImgService.findImgListByIdList(anyList())).thenReturn(friendsImgList);

        //when
        List<ResponseFriendshipWithImg> result = friendshipService.findFriendList(receiverEmail);

        //then
        assertEquals(result.size(), 2);
        assertEquals(result.get(0).getFriendshipDto().getStatus(), ACCEPTED);
        assertEquals(result.get(1).getFriendshipDto().getStatus(), ACCEPTED);
        assertEquals(result.get(0).getFriendshipDto().getReceiverDto().getEmail(), receiverEmail);
        assertEquals(result.get(1).getFriendshipDto().getReceiverDto().getEmail(), receiverEmail);
        assertEquals(result.get(0).getImg().getUrl(), "test url");
        assertEquals(result.get(1).getImg().getUrl(), "test url2");
        verify(memberService).findByEmail(anyString());
        verify(friendshipCustomRepository).findFriendListByMember(any(Member.class));
        verify(memberImgService).findImgListByIdList(anyList());

    }
    @Test
    @DisplayName("친구 서비스: 친구가 제대로 삭제 되는지 테스트")
    void testDeleteFriendship(){
        //given
        // 친구요청 받은 멤버
        String receiverEmail = "receiver@email.com";
        Member receiver = getTestReceiver(receiverEmail);
        // 친구 요청 보낸 멤버
        String senderEmail = "sender@email.com";
        Member sender = getTestSender(senderEmail);

        Friendship friendship = Friendship.builder().receiver(receiver).status(ACCEPTED).sender(sender).build();
        RequestDeleteFriendship requestDeleteFriendship = RequestDeleteFriendship.builder().friendshipId(1L).build();

        when(friendshipRepository.findById(anyLong())).thenReturn(Optional.ofNullable(friendship));
        when(memberService.findByEmail(anyString())).thenReturn(receiver);
        //when
        friendshipService.deleteFriendship(requestDeleteFriendship, receiverEmail);
        //then
        verify(friendshipRepository).delete(any(Friendship.class));
    }

    private List<Friendship> getWaitingFriendshipList(Member receiver, Member sender, Member secondSender){
        Friendship friendship_1 = Friendship.builder().receiver(receiver).sender(sender).status(WAITING).build();
        Friendship friendship_2 = Friendship.builder().receiver(receiver).sender(secondSender).status(WAITING).build();
        List<Friendship> list = new ArrayList<>();
        list.add(friendship_1); list.add(friendship_2);
        return list;
    }
    private List<MemberImage> getFriendsImgList(Member sender, Member secondSender){
        MemberImage memberImage_1 = new MemberImage(1L, "test url", 1, sender);
        MemberImage memberImage_2 = new MemberImage(2L, "test url2", 1, secondSender);
        List<MemberImage> memberImageList = new ArrayList<>();
        memberImageList.add(memberImage_1); memberImageList.add(memberImage_2);
        return memberImageList;
    }
    private List<Friendship> getFriendshipList(Member receiver, Member sender, Member secondSender){
        Friendship friendship_1 = Friendship.builder().receiver(receiver).sender(sender).status(ACCEPTED).build();
        Friendship friendship_2 = Friendship.builder().receiver(receiver).sender(secondSender).status(ACCEPTED).build();
        List<Friendship> list = new ArrayList<>();
        list.add(friendship_1); list.add(friendship_2);
        return list;
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
