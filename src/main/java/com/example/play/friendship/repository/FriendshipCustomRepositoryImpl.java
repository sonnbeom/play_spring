package com.example.play.friendship.repository;

import com.example.play.friendship.constant.FriendshipStatus;
import com.example.play.friendship.entity.Friendship;
import com.example.play.friendship.entity.QFriendship;
import com.example.play.friendship.exception.FriendshipDeleteException;
import com.example.play.member.entity.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
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

    @Override
    public List<Friendship> findFriendListByMember(Member member) {
        return jpaQueryFactory
                .selectFrom(friendship)
                .innerJoin(friendship.member)
                .fetchJoin()
                .where(friendship.member.eq(member)
                        .and(friendship.status.eq(ACCEPTED)))
                .orderBy(friendship.createdAt.desc())
                .fetch();
    }

    @Override
    public boolean delete(Friendship entity) {
        try {
            long count = jpaQueryFactory
                    .delete(friendship)
                    .where(friendship.eq(entity))
                    .execute();
            return count > 0;
        } catch (Exception e){
            log.info("friendship 엔티티를 삭제하지 못 하였습니다. 삭제하려는 엔티티 아이디: {}", entity.getId(), e);
            throw new FriendshipDeleteException("friendship 엔티티를 삭제하지 못하였습니다. {}", e);
        }
    }
}
