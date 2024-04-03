package com.example.play.friendship.service;

import com.example.play.friendship.dto.RequestFriendship;
import com.example.play.friendship.dto.ResponseFriendship;
import com.example.play.friendship.dto.ResponseFriendshipDto;
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

        Member fromMember = memberService.findByEmail(friendship.getFromMemberEmail());
        Member toMember = memberService.findByEmail(friendship.getToMemberEmail());

        Friendship friendshipFrom = friendshipMapper.fromDtoToEntity(fromMember, toMember);
        Friendship friendshipTo = friendshipMapper.toDtoToEntity(fromMember, toMember);

        friendshipRepository.save(friendshipTo);
        friendshipRepository.save(friendshipTo);

        friendshipFrom.setCounterpartId(friendshipTo.getId());
        friendshipTo.setCounterpartId(friendshipFrom.getId());
        ResponseFriendshipDto responseFriendshipFrom = friendshipMapper.entityToDto(friendshipFrom);
        ResponseFriendshipDto responseFriendshipTo = friendshipMapper.entityToDto(friendshipTo);
        return ResponseFriendship.builder()
                .fromFriendship(responseFriendshipFrom)
                .toFriendship(responseFriendshipTo)
                .build();
    }
}
