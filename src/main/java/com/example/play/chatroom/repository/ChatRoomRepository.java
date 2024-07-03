package com.example.play.chatroom.repository;

import com.example.play.chatroom.domain.ChatRoom;
import com.example.play.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findByMemberAndOther(Member member, Member other);
}
