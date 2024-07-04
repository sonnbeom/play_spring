package com.example.play.chat.repository;

import com.example.play.chat.domain.ChatMessage;
import com.example.play.chatroom.domain.ChatRoom;
import com.example.play.chat.domain.QChatMessage;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.play.chat.domain.QChatMessage.chatMessage;

@Repository
public class CustomChatMessageRepositoryImpl implements CustomChatMessageRepository{
    private final JPAQueryFactory jpaQueryFactory;

    public CustomChatMessageRepositoryImpl(EntityManager entityManager) {
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
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
    public List<ChatMessage> getChatsByRoomList(List<ChatRoom> chatRoomList) {
        QChatMessage subChat = new QChatMessage("subChat");
        List<ChatMessage> chatMessages = jpaQueryFactory
                .selectFrom(chatMessage)
                .where(chatMessage.createdAt.in(
                        JPAExpressions
                                .select(subChat.createdAt.max())
                                .from(subChat)
                                .where(subChat.chatRoom.in(chatRoomList))
                                .groupBy(subChat.chatRoom.id)
                ))
                .fetch();
        return chatMessages;
    }

    @Override
    public List<ChatMessage> findByRoomNumber(ChatRoom chatRoom) {
        return jpaQueryFactory
                .selectFrom(chatMessage)
                .where(chatMessage.chatRoom.eq(chatRoom))
                .orderBy(chatMessage.createdAt.desc())
                .limit(30)
                .fetch();
    }

}
