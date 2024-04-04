package com.example.play.friendship.repository;

import com.example.play.friendship.constant.FriendshipStatus;
import com.example.play.friendship.entity.Friendship;
import com.example.play.friendship.entity.QFriendship;
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

    @Override
    public List<Friendship> findWaitingRequestByEmail(String email) {
        return jpaQueryFactory
                .selectFrom(friendship)
                .where(friendship.memberEmail.eq(email)
                        .and(friendship.isFrom.eq(false))
                        .and(friendship.status.eq(WAITING)))
                .fetch();
    }

    public List<Friendship> z(String email) {
        return jpaQueryFactory
                .selectFrom(friendship)
                .f
    }

}
