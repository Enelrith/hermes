package io.github.enelrith.hermes.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response returned after a successful user login")
public record AuthResponse(@Schema(description = "User's newly generated JWT access token. Expires in 30 minutes", example = "jwt-access-token")
                           String accessToken,
                           @Schema(description = "The time period that will take for the access token to expire in milliseconds", example = "1800000")
                           Long expiresIn,
                           @Schema(example = "user@gmail.com")
                           String email,
                           @Schema(description = "User's newly generated JWT refresh token. Expires in 7 days", example = "jwt-refresh-token")
                           String refreshToken) {
}
