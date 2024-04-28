package com.example.play.friendship.mapper;


import com.example.play.friendship.dto.ResponseFriendshipDto;
import com.example.play.friendship.dto.ResponseFriendListDto;
import com.example.play.friendship.entity.Friendship;
import com.example.play.image.constant.ImgConstant;
import com.example.play.jwt.constant.HeaderConstant;
import com.example.play.member.entity.Member;
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
