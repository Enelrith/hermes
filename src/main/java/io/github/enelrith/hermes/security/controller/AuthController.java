package io.github.enelrith.hermes.security.controller;

import io.github.enelrith.hermes.common.exception.ErrorResponse;
import io.github.enelrith.hermes.security.dto.AuthRequest;
import io.github.enelrith.hermes.security.dto.AuthResponse;
import io.github.enelrith.hermes.security.dto.LogoutRequest;
import io.github.enelrith.hermes.security.dto.RefreshTokenRequest;
import io.github.enelrith.hermes.security.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@SecurityRequirements(value = {})
@Tag(name = "Auth Controller", description = "Handles all operations regarding user authentication")
public class AuthController {
    private final AuthService authService;

    @Operation(summary = "Login user", description = "Authenticates the user and returns the user's email, access and refresh tokens")
    @ApiResponse(
            responseCode = "200",
            description = "User authenticated",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AuthResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "Invalid username or password",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)
            )
    )
    @PostMapping
    public ResponseEntity<AuthResponse> loginUser(@Valid @RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.loginUser(request));
    }

    @Operation(summary = "Refresh token", description = "Returns the user's email, a new access token, and a new refresh token")
    @ApiResponse(
            responseCode = "200",
            description = "Token refreshed",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AuthResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "Invalid refresh token",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)
            )
    )
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(authService.refresh(request));
    }

    @Operation(summary = "Logout user", description = "Deletes the user's refresh token")
    @ApiResponse(
            responseCode = "200",
            description = "User logged out"
    )
    @ApiResponse(
            responseCode = "400",
            description = "Invalid refresh token or invalid user",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)
            )
    )
    @PostMapping("/logout")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Void> logoutUser(@Valid @RequestBody LogoutRequest request) {
        authService.logoutUser(request);
        return ResponseEntity.ok().build();
    }
}
