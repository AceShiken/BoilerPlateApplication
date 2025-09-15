package com.demo.repository;

import com.demo.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByToken(String token);
    long deleteByExpiresAtBefore(Instant cutoff);
}

