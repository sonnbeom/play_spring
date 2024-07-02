package com.example.play.friendship.repository;

import com.example.play.friendship.domain.Friendship;
import com.example.play.member.domain.Member;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FriendshipCustomRepository {

    List<Friendship> findWaitinFrinedshipList(Member member, Pageable pageable);

    List<Friendship> findFriendListByMember(Member member);
}
