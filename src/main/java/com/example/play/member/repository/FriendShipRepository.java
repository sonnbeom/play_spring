package com.example.play.member.repository;

import com.example.play.member.entity.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendShipRepository extends JpaRepository<Friendship, Long> {
}
