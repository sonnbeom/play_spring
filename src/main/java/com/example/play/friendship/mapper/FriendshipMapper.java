package com.example.play.friendship.mapper;


import com.example.play.friendship.domain.Friendship;
import com.example.play.member.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.example.play.friendship.constant.FriendshipStatus.*;

@RequiredArgsConstructor
@Component
@Slf4j
public class FriendshipMapper {
    public Friendship dtoToEntity(Member sender, Member receiver) {
        return Friendship.builder()
                .sender(sender)
                .receiver(receiver)
                .status(WAITING)
                .build();
    }
}
