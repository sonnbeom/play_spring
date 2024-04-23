package com.example.play.chat.repository;

import com.example.play.chat.domain.ChatRoom;
import com.example.play.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findByMemberAndOther(Member member, Member other);
}
