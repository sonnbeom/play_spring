package com.example.play.jwt.repository;

import com.example.play.jwt.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<RefreshToken, String> {
    boolean existsByRefreshToken(String refreshToken);
}
