package com.example.play.friendship.mapper;


import com.example.play.friendship.dto.ResponseFriendshipDto;
import com.example.play.friendship.entity.Friendship;
import com.example.play.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.example.play.friendship.entity.Friendship.FriendshipStatus.WAITING;

@RequiredArgsConstructor
@Component
@Slf4j
public class FriendshipMapper {

    public Friendship fromDtoToEntity(Member fromMember, Member toMember) {
        return Friendship.builder()
                .isFrom(true)
                .member(fromMember)
                .memberEmail(fromMember.getEmail())
                .friendEmail(toMember.getEmail())
                .status(WAITING)
                .build();
    }

    public Friendship toDtoToEntity(Member fromMember, Member toMember) {
        return Friendship.builder()
                .isFrom(false)
                .member(toMember)
                .memberEmail(toMember.getEmail())
                .friendEmail(fromMember.getEmail())
                .status(WAITING)
                .build();
    }


    public ResponseFriendshipDto entityToDto(Friendship friendshipFrom) {
        return ResponseFriendshipDto.builder()
                .isFrom(friendshipFrom.isFrom())
                .friendEmail(friendshipFrom.getFriendEmail())
                .counterpartId(friendshipFrom.getCounterpartId())
                .memberEmail(friendshipFrom.getMemberEmail())
                .status(friendshipFrom.getStatus())
                .id(friendshipFrom.getId())
                .build();
    }
}
