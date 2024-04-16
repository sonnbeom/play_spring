package com.example.play.jwt.repository;

import com.example.play.jwt.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<RefreshToken, String> {
    boolean existsByRefreshToken(String refreshToken);
    boolean findByEmailAndRefreshToken(String email, String refreshToken);
}
