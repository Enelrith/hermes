package io.github.enelrith.hermes.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Request body for logging a user in")
public record AuthRequest(@Schema(
                                example = "user@gmail.com",
                                requiredMode = Schema.RequiredMode.REQUIRED
                          )
                          @Email(message = "Must be a valid email")
                          @NotBlank(message = "Email cannot be empty")
                          String email,
                          @Schema(
                                  example = "123456",
                                  requiredMode = Schema.RequiredMode.REQUIRED
                          )
                          @NotBlank(message = "Password cannot be empty")
                          @Size(min = 6, max = 72, message = "Password must be between {min} and {max} characters long")
                          String password) {
}
