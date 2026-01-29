package io.github.enelrith.hermes.security.repository;

import io.github.enelrith.hermes.security.entity.RefreshToken;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    @EntityGraph(attributePaths = {"user", "user.roles"})
    Optional<RefreshToken> findByToken(String refreshToken);
}