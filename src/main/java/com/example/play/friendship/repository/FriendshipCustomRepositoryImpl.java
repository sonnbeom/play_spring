package com.example.play.friendship.repository;

import com.example.play.friendship.constant.FriendshipStatus;
import com.example.play.friendship.entity.Friendship;
import com.example.play.friendship.entity.QFriendship;
import com.example.play.member.entity.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.play.friendship.constant.FriendshipStatus.*;
import static com.example.play.friendship.entity.QFriendship.*;

@Repository
public class FriendshipCustomRepositoryImpl implements FriendshipCustomRepository{
    private final JPAQueryFactory jpaQueryFactory;

    public FriendshipCustomRepositoryImpl(EntityManager entityManager) {
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }


    /*
    * 받은 친구 요청 리스트를 조회
    * */
    @Override
    public List<Friendship> findWaitinFrinedshipList(Member member) {
        return jpaQueryFactory
                .selectFrom(friendship)
                .innerJoin(friendship.member)
                .fetchJoin()
                .where(friendship.member.eq(member)
                        .and(friendship.isFrom.eq(false))
                        .and(friendship.status.eq(WAITING)))
                .orderBy(friendship.createdAt.desc())
                .fetch();
    }

}
