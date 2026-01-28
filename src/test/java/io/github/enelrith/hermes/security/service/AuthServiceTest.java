package io.github.enelrith.hermes.security.service;

import io.github.enelrith.hermes.security.CustomUserDetails;
import io.github.enelrith.hermes.security.dto.*;
import io.github.enelrith.hermes.security.entity.RefreshToken;
import io.github.enelrith.hermes.security.entity.RefreshTokenRepository;
import io.github.enelrith.hermes.security.exception.InvalidRefreshTokenException;
import io.github.enelrith.hermes.security.exception.InvalidUserException;
import io.github.enelrith.hermes.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock private AuthenticationManager authenticationManager;
    @Mock private RefreshTokenRepository refreshTokenRepository;
    @Mock private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        // Injecting @Value fields manually
        ReflectionTestUtils.setField(authService, "accessTokenExpiration", 1800000L);
        ReflectionTestUtils.setField(authService, "refreshTokenExpiration", 604800000L);
    }

    @Test
    @DisplayName("Login: Should return AuthResponse and save token when credentials are valid")
    void loginUser_Success() {
        var request = new AuthRequest("test@mail.com", "password");
        var user = new User();
        user.setEmail("test@mail.com");
        var userDetails = new CustomUserDetails(user);

        Authentication auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn(userDetails);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);
        when(jwtService.generateAccessToken(any())).thenReturn("access-token");
        when(jwtService.generateRefreshToken(any())).thenReturn("refresh-token");

        AuthResponse response = authService.loginUser(request);

        assertNotNull(response);
        assertEquals("access-token", response.accessToken());
        assertEquals("refresh-token", response.refreshToken());
        verify(refreshTokenRepository).save(any(RefreshToken.class));
    }

    @Test
    @DisplayName("Refresh: Should rotate tokens and delete old one")
    void refresh_Success() {
        var oldTokenStr = "old-refresh";
        var user = new User();
        user.setEmail("user@mail.com");

        var oldTokenEntity = new RefreshToken();
        oldTokenEntity.setToken(oldTokenStr);
        oldTokenEntity.setUser(user);
        oldTokenEntity.setExpiryDate(Instant.now().plusSeconds(100)); // Not expired

        when(refreshTokenRepository.findByToken(oldTokenStr)).thenReturn(Optional.of(oldTokenEntity));
        when(jwtService.generateAccessToken(any())).thenReturn("new-access");
        when(jwtService.generateRefreshToken(any())).thenReturn("new-refresh");

        AuthResponse response = authService.refresh(new RefreshTokenRequest(oldTokenStr));

        assertEquals("new-access", response.accessToken());
        verify(refreshTokenRepository).delete(oldTokenEntity); // Verify rotation
        verify(refreshTokenRepository).save(any(RefreshToken.class));
    }

    @Test
    @DisplayName("Refresh: Should throw and delete if token is expired")
    void refresh_ExpiredToken() {
        var expiredToken = new RefreshToken();
        expiredToken.setExpiryDate(Instant.now().minusSeconds(10));

        when(refreshTokenRepository.findByToken("expired")).thenReturn(Optional.of(expiredToken));

        assertThrows(InvalidRefreshTokenException.class, () -> authService.refresh(new RefreshTokenRequest("expired")));
        verify(refreshTokenRepository).delete(expiredToken);
    }

    @Test
    @DisplayName("Logout: Should delete token if email matches")
    void logoutUser_Success() {
        var user = new User();
        user.setEmail("owner@mail.com");
        var token = new RefreshToken();
        token.setUser(user);

        when(refreshTokenRepository.findByToken("token123")).thenReturn(Optional.of(token));

        authService.logoutUser(new LogoutRequest("owner@mail.com", "token123"));

        verify(refreshTokenRepository).delete(token);
    }

    @Test
    @DisplayName("Logout: Should throw if user tries to logout someone else's token")
    void logoutUser_InvalidUser() {
        var owner = new User();
        owner.setEmail("owner@mail.com");
        var token = new RefreshToken();
        token.setUser(owner);

        when(refreshTokenRepository.findByToken("token123")).thenReturn(Optional.of(token));

        LogoutRequest maliciousRequest = new LogoutRequest("hacker@mail.com", "token123");
        assertThrows(InvalidUserException.class, () -> authService.logoutUser(maliciousRequest));
        verify(refreshTokenRepository, never()).delete(any());
    }
}
