package io.github.enelrith.hermes.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Request body for registering a new user in the database")
public record RegisterUserRequest(@Schema(example = "user@gmail.com", requiredMode = Schema.RequiredMode.REQUIRED)
                                  @Email(message = "Must be a valid email")
                                  @NotBlank(message = "Email cannot be empty")
                                  @Size(min = 1, max = 255, message = "Email must be between {min} and {max} characters long")
                                  String email,
                                  @Schema(example = "123456", requiredMode = Schema.RequiredMode.REQUIRED)
                                  @NotBlank(message = "Password cannot be empty")
                                  @Size(min = 6, max = 72, message = "Password must be between {min} and {max} characters long")
                                  String password,
                                  @Schema(description = "Repeat the user password", example = "123456", requiredMode = Schema.RequiredMode.REQUIRED)
                                  @NotBlank(message = "Repeat password cannot be empty")
                                  @Size(min = 6, max = 72, message = "Repeat password must be between {min} and {max} characters long")
                                  String rePassword) {
}
