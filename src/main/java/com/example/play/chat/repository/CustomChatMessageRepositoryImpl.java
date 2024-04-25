package com.example.play.chat.repository;

import com.example.play.chat.domain.ChatMessage;
import com.example.play.chat.domain.QChatMessage;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.hibernate.query.criteria.JpaSubQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

import static com.example.play.chat.domain.QChatMessage.chatMessage;

@Repository
public class CustomChatMessageRepositoryImpl implements CustomChatMessageRepository{
    private final JPAQueryFactory jpaQueryFactory;

    public CustomChatMessageRepositoryImpl(EntityManager entityManager) {
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }


    @Override
    public List<ChatMessage> findChatMessagesByChatRoom(Long chatRoomId) {
        return null;
    }

    @Override
    public Page<ChatMessage> getChats(Pageable pageable, Long chatRoomId) {

        List<ChatMessage> chatMessages = jpaQueryFactory
                .selectFrom(chatMessage)
                .where(chatMessage.chatRoom.id.eq(chatRoomId))
                .orderBy(chatMessage.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        JPAQuery<Long> count = jpaQueryFactory
                .select(chatMessage.count())
                .from(chatMessage)
                .where(chatMessage.chatRoom.id.eq(chatRoomId));
        return PageableExecutionUtils.getPage(chatMessages, pageable, count::fetchOne);
    }

    @Override
    public List<ChatMessage> getChatsByRoomIdList(List<Long> chatRoomIdList) {
        QChatMessage subChat = new QChatMessage("subChat");
        List<ChatMessage> chatMessages = jpaQueryFactory
                .selectFrom(chatMessage)
                .where(chatMessage.createdAt.in(
                        JPAExpressions
                                .select(subChat.createdAt.max())
                                .from(subChat)
                                .where(subChat.chatRoom.id.in(chatRoomIdList))
                                .groupBy(subChat.chatRoom.id)

                ))
                .fetch();
        return chatMessages;
    }

}
