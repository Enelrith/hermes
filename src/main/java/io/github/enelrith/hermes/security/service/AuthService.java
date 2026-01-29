package io.github.enelrith.hermes.security.service;

import io.github.enelrith.hermes.security.CustomUserDetails;
import io.github.enelrith.hermes.security.exception.InvalidRefreshTokenException;
import io.github.enelrith.hermes.security.dto.AuthRequest;
import io.github.enelrith.hermes.security.dto.AuthResponse;
import io.github.enelrith.hermes.security.dto.LogoutRequest;
import io.github.enelrith.hermes.security.dto.RefreshTokenRequest;
import io.github.enelrith.hermes.security.entity.RefreshToken;
import io.github.enelrith.hermes.security.repository.RefreshTokenRepository;
import io.github.enelrith.hermes.security.exception.InvalidUserException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;

    @Value("${JWT_EXPIRATION}")
    private Long accessTokenExpiration;

    @Value("${spring.security.jwt.refresh-expiration}")
    private Long refreshTokenExpiration;

    @Transactional
    public AuthResponse loginUser(AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        String accessToken = jwtService.generateAccessToken(customUserDetails);
        RefreshToken refreshTokenEntity = generateRefreshTokenEntity(customUserDetails);

        refreshTokenRepository.save(refreshTokenEntity);

        return new AuthResponse(accessToken, accessTokenExpiration, customUserDetails.getUsername(), refreshTokenEntity.getToken());
    }

    @Transactional
    public AuthResponse refresh(RefreshTokenRequest request) {
        var usedRefreshToken = refreshTokenRepository.findByToken(request.refreshToken()).orElseThrow(InvalidRefreshTokenException::new);
        if (usedRefreshToken.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(usedRefreshToken);
            throw new InvalidRefreshTokenException("Refresh token has expired");
        }

        var user = usedRefreshToken.getUser();
        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        String accessToken = jwtService.generateAccessToken(customUserDetails);

        refreshTokenRepository.delete(usedRefreshToken);
        RefreshToken refreshTokenEntity = generateRefreshTokenEntity(customUserDetails);

        refreshTokenRepository.save(refreshTokenEntity);

        return new AuthResponse(accessToken, accessTokenExpiration, customUserDetails.getUsername(), refreshTokenEntity.getToken());
    }

    @Transactional
    @PreAuthorize("#request.email() == authentication.name")
    public void logoutUser(LogoutRequest request) {
        RefreshToken tokenEntity = refreshTokenRepository.findByToken(request.refreshToken())
                .orElseThrow(InvalidRefreshTokenException::new);

        if (!tokenEntity.getUser().getEmail().equals(request.email())) {
            throw new InvalidUserException("Token does not belong to this user");
        }

        refreshTokenRepository.delete(tokenEntity);
        SecurityContextHolder.clearContext();
    }

    private RefreshToken generateRefreshTokenEntity(CustomUserDetails customUserDetails) {
        String refreshToken = jwtService.generateRefreshToken(customUserDetails);

        RefreshToken refreshTokenEntity = new RefreshToken();
        refreshTokenEntity.setToken(refreshToken);
        refreshTokenEntity.setExpiryDate(Instant.now().plusMillis(refreshTokenExpiration));
        refreshTokenEntity.setCreatedAt(Instant.now());
        refreshTokenEntity.setUser(customUserDetails.getUser());

        return refreshTokenEntity;
    }
}
