package com.example.play.Member.repository;

import com.example.play.Member.entity.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendShipRepository extends JpaRepository<Friendship, Long> {
}
