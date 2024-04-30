package com.example.play.friendship.repository;


import com.example.play.friendship.entity.Friendship;
import com.example.play.member.entity.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.play.friendship.constant.FriendshipStatus.*;
import static com.example.play.friendship.entity.QFriendship.*;

@Repository
@Slf4j
public class FriendshipCustomRepositoryImpl implements FriendshipCustomRepository{
    private final JPAQueryFactory jpaQueryFactory;

    public FriendshipCustomRepositoryImpl(EntityManager entityManager) {
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    // 받은 친구 신청 리스트 조회
    @Override
    public List<Friendship> findWaitinFrinedshipList(Member member, Pageable pageable) {
        return jpaQueryFactory.selectFrom(friendship)
                .innerJoin(friendship.receiver).fetchJoin()
                .innerJoin(friendship.sender).fetchJoin()
                .where(friendship.receiver.eq(member)
                        .or(friendship.sender.eq(member))
                        .and(friendship.status.eq(WAITING)))
                .orderBy(friendship.createdAt.desc())
                .fetch();
    }
    @Override
    public List<Friendship> findFriendListByMember(Member member) {
        return jpaQueryFactory.selectFrom(friendship)
                .innerJoin(friendship.sender).fetchJoin()
                .innerJoin(friendship.receiver).fetchJoin()
                .where((friendship.sender.eq(member)
                        .or(friendship.receiver.eq(member)))
                        .and(friendship.status.eq(ACCEPTED)))
                .orderBy(friendship.createdAt.desc())
                .fetch();
    }
}
