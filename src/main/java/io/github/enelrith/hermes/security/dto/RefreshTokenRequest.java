package io.github.enelrith.hermes.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request body for refreshing a user's JWT access token")
public record RefreshTokenRequest(@Schema(description = "The user's valid refresh token", example = "jwt-refresh-token", requiredMode = Schema.RequiredMode.REQUIRED)
                                  String refreshToken) {
}
