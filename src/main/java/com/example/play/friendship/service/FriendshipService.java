package com.example.play.friendship.service;

import com.example.play.friendship.dto.RequestFriendship;
import com.example.play.friendship.dto.ResponseFriendship;
import com.example.play.friendship.dto.ResponseFriendshipDto;
import com.example.play.friendship.dto.WaitingFriendListDto;
import com.example.play.friendship.entity.Friendship;
import com.example.play.friendship.mapper.FriendshipMapper;
import com.example.play.friendship.repository.FriendshipCustomRepository;
import com.example.play.friendship.repository.FriendshipRepository;
import com.example.play.member.entity.Member;
import com.example.play.member.service.MemberService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FriendshipService {
    private final FriendshipCustomRepository friendshipCustomRepository;
    private final FriendshipRepository friendshipRepository;
    private final MemberService memberService;
    private final FriendshipMapper friendshipMapper;

    public ResponseFriendship create(RequestFriendship friendship) {
        // 친구요청 보내는 이와 받는 이를 조회 isFrom = true : 보내는 사람
        Member fromMember = memberService.findByEmail(friendship.getFromMemberEmail());
        Member toMember = memberService.findByEmail(friendship.getToMemberEmail());

        //친구 요청 보내는 멤버, 받는 멤버를 친구 요청 엔티티로 매핑
        Friendship friendshipFrom = friendshipMapper.fromDtoToEntity(fromMember, toMember);
        Friendship friendshipTo = friendshipMapper.toDtoToEntity(fromMember, toMember);

        friendshipRepository.save(friendshipTo);
        friendshipRepository.save(friendshipFrom);

        // 레포지토리에 저장해야 아이디가 나오기에 저장 후 서로의 아이디 매핑
        friendshipFrom.setCounterpartId(friendshipTo.getId());
        friendshipTo.setCounterpartId(friendshipFrom.getId());

        //엔티티틀 클라이언트 요청에 맞게 매핑하여 리턴
        ResponseFriendshipDto responseFriendshipFrom = friendshipMapper.entityToDto(friendshipFrom);
        ResponseFriendshipDto responseFriendshipTo = friendshipMapper.entityToDto(friendshipTo);
        return ResponseFriendship.builder()
                .fromFriendship(responseFriendshipFrom)
                .toFriendship(responseFriendshipTo)
                .build();
    }

    public List<WaitingFriendListDto> getWaitingFriendList(String email) {
        //현재 로그인한 멤버의 정보를 불러온다.
        Member member = memberService.findByEmail(email);
        List<Friendship> waitingFriendList = friendshipCustomRepository.findWaitingRequestByEmail(email);
        return friendshipMapper.listEntityToDto(waitingFriendList);
    }
}
