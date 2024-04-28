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




    public ResponseFriendListDto EntityToDtoWithImg(Friendship friendship, String url) {
        return ResponseFriendListDto.builder()
                .friendNickname(friendship.getFriendNickname())
                .imgUrl(url)
                .memberEmail(friendship.getMemberEmail())
                .memberId(friendship.getMember().getId())
                .friendshipId(friendship.getId())
                .friendEmail(friendship.getFriendEmail())
                .time(friendship.getCreatedAt())
                .status(friendship.getStatus())
                .build();
    }

    public ResponseFriendListDto entityToDtoWithoutImg(Friendship friendship) {
       return ResponseFriendListDto.builder()
                        .friendNickname(friendship.getFriendNickname())
                        .imgUrl(String.valueOf(ImgConstant.DEFAULT))
                        .friendshipId(friendship.getId())
                        .memberEmail(friendship.getMemberEmail())
                        .memberId(friendship.getMember().getId())
                        .friendEmail(friendship.getFriendEmail())
                        .time(friendship.getCreatedAt())
                        .status(friendship.getStatus())
                        .build();
    }

    public Friendship dtoToEntityBySend(Member sender, Member receiver) {
        return Friendship.builder()
                .isReceived(false)
                .sender(sender)
                .receiver(receiver)
                .status(WAITING)
                .build();
    }

    public Friendship dtoToEntityByReceived(Member sender, Member receiver) {
        return Friendship.builder()
                .isReceived(true)
                .sender(sender)
                .receiver(receiver)
                .status(WAITING)
                .build();
    }

}
