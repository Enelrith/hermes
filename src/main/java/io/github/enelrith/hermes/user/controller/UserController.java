package io.github.enelrith.hermes.user.controller;

import io.github.enelrith.hermes.common.exception.ErrorResponse;
import io.github.enelrith.hermes.user.dto.RegisterUserRequest;
import io.github.enelrith.hermes.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "User Controller", description = "Handles all operations regarding the user")
@SecurityRequirements(value = {})
public class UserController {
    private final UserService userService;

    @Operation(summary = "Registers a new user", description = "Creates a new user with the USER role if the credentials in the request body are valid")
    @ApiResponse(
            responseCode = "201",
            description = "User registered"
    )
    @ApiResponse(
            responseCode = "400",
            description = "Invalid credentials or USER role not found in the database",
            content = @Content(
                    schema = @Schema(implementation = ErrorResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "409",
            description = "A user with this email already exists",
            content = @Content(
                    schema = @Schema(implementation = ErrorResponse.class)
            )
    )
    @PostMapping
    public ResponseEntity<Void> registerUser(@Valid @RequestBody RegisterUserRequest request) {
        userService.registerUser(request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
