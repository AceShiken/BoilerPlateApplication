package com.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Index;

import java.time.Instant;

@Entity
@Table(name = "tokens")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Index(name = "idx_token_value")
    @Column(name = "token", nullable = false, unique = true, length = 512)
    private String token;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column(name = "revoked", nullable = false)
    private boolean revoked = false;

    @PrePersist
    public void prePersist() {
        if (expiresAt == null) {
            // set by service explicitly; no-op here to avoid surprises
        }
    }
}
