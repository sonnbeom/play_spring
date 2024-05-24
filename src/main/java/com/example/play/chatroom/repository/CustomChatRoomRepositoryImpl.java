package com.example.play.chatroom.repository;

import com.example.play.chatroom.domain.ChatRoom;
import com.example.play.chatroom.domain.QChatRoom;
import com.example.play.member.entity.Member;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.play.chatroom.domain.QChatRoom.*;


@Repository
public class CustomChatRoomRepositoryImpl implements CustomChatRoomRepository{
    private final JPAQueryFactory jpaQueryFactory;

    public CustomChatRoomRepositoryImpl(EntityManager entityManager) {
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }
    @Override
    public Page<ChatRoom> findRooms(Member member, Pageable pageable) {
        List<ChatRoom> chatRooms = jpaQueryFactory
                .selectFrom(chatRoom)
                .where(chatRoom.member.eq(member))
                .orderBy(chatRoom.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        JPAQuery<Long> count = jpaQueryFactory
                .select(chatRoom.count())
                .from(chatRoom)
                .where(chatRoom.member.eq(member));

        return PageableExecutionUtils.getPage(chatRooms, pageable, count::fetchOne);
    }
}
