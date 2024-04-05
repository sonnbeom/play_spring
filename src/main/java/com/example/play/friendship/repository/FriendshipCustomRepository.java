package com.example.play.friendship.repository;

import com.example.play.friendship.entity.Friendship;
import com.example.play.member.entity.Member;

import java.util.List;

public interface FriendshipCustomRepository {

    List<Friendship> findWaitinFrinedshipList(Member member);

    List<Friendship> findFriendListByMember(Member member);
    boolean delete(Friendship friendship);
}
