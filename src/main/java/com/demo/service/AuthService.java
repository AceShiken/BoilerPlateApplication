package com.demo.service;

import com.demo.entity.Role;
import com.demo.entity.Token;
import com.demo.entity.User;
import com.demo.exception.NotFoundException;
import com.demo.repository.TokenRepository;
import com.demo.repository.UserRepository;
import com.demo.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final JwtUtils jwtUtils;

    /**
     * Issues a bearer token if (userId, phone) matches.
     */
    @Transactional
    public String issueToken(Long userId, String phone) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found: " + userId));

        if (user.getPhone() == null || !user.getPhone().equals(phone)) {
            throw new NotFoundException("Invalid userId/phone combination");
        }

        String jwt = jwtUtils.createToken(user.getId(), user.getRole().name());
        Instant exp = jwtUtils.getExpiry(jwt);

        tokenRepository.save(Token.builder()
                .token(jwt)
                .userId(user.getId())
                .expiresAt(exp)
                .revoked(false)
                .build());

        return jwt;
    }

    /**
     * Validates a token against signature, expiry and DB presence/revocation.
     * Returns the user if valid.
     */
    @Transactional(readOnly = true)
    public User validateTokenAndGetUser(String bearer) {
        var jws = jwtUtils.parse(bearer);
        Long userId = Long.valueOf(jws.getBody().getSubject());
        Instant exp = jws.getBody().getExpiration().toInstant();

        // Check stored token
        Token t = tokenRepository.findByToken(bearer)
                .orElseThrow(() -> new NotFoundException("Token not recognized"));

        if (t.isRevoked() || t.getExpiresAt().isBefore(Instant.now()) || exp.isBefore(Instant.now())) {
            throw new NotFoundException("Token expired or revoked");
        }
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found for token"));
    }

    @Transactional
    public void revokeToken(String bearer) {
        tokenRepository.findByToken(bearer).ifPresent(t -> {
            t.setRevoked(true);
            tokenRepository.save(t);
        });
    }
}
