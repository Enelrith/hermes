package io.github.enelrith.hermes.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request body for logging a user out by deleting their current refresh token")
public record LogoutRequest(@Schema(example = "user@gmail.com")
                            String email,
                            @Schema(description = "The user's valid refresh token", example = "jwt-refresh-token")
                            String refreshToken) {
}
