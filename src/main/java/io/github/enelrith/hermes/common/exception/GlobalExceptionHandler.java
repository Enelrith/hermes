package io.github.enelrith.hermes.common.exception;

import io.github.enelrith.hermes.security.exception.InvalidRefreshTokenException;
import io.github.enelrith.hermes.security.exception.InvalidUserException;
import io.github.enelrith.hermes.user.exception.EmailAlreadyInUseException;
import io.github.enelrith.hermes.user.exception.PasswordsDoNotMatchException;
import io.github.enelrith.hermes.user.exception.RoleNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ErrorResponse buildErrorResponse(HttpStatus status, String message,
                                            String path, Map<String, String> validationErrors) {
        return ErrorResponse.builder()
                .timestamp(Instant.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(path)
                .validationErrors(validationErrors)
                .build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e, HttpServletRequest request) {

        ErrorResponse response = buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                e.getMessage(),
                request.getServletPath(),
                null
        );

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleException(MethodArgumentNotValidException e, HttpServletRequest request) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        ErrorResponse response = buildErrorResponse(HttpStatus.BAD_REQUEST, "One or more fields are invalid",
                request.getServletPath(), errors);

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleException(AuthenticationException e, HttpServletRequest request) {

        ErrorResponse response = buildErrorResponse(HttpStatus.UNAUTHORIZED, "Invalid email or password",
                request.getServletPath(), null);

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @ExceptionHandler(HttpClientErrorException.TooManyRequests.class)
    public ResponseEntity<ErrorResponse> handleException(HttpClientErrorException.TooManyRequests e, HttpServletRequest request) {

        ErrorResponse response = buildErrorResponse(HttpStatus.TOO_MANY_REQUESTS, "Too many requests",
                request.getServletPath(), null);

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @ExceptionHandler(EmailAlreadyInUseException.class)
    public ResponseEntity<ErrorResponse> handleException(EmailAlreadyInUseException e, HttpServletRequest request) {

        ErrorResponse response = buildErrorResponse(HttpStatus.CONFLICT, e.getMessage(),
                request.getServletPath(), null);

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @ExceptionHandler(PasswordsDoNotMatchException.class)
    public ResponseEntity<ErrorResponse> handleException(PasswordsDoNotMatchException e, HttpServletRequest request) {

        ErrorResponse response = buildErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage(),
                request.getServletPath(), null);

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @ExceptionHandler(InvalidRefreshTokenException.class)
    public ResponseEntity<ErrorResponse> handleException(InvalidRefreshTokenException e, HttpServletRequest request) {

        ErrorResponse response = buildErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage(),
                request.getServletPath(), null);

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @ExceptionHandler(InvalidUserException.class)
    public ResponseEntity<ErrorResponse> handleException(InvalidUserException e, HttpServletRequest request) {

        ErrorResponse response = buildErrorResponse(HttpStatus.FORBIDDEN, e.getMessage(),
                request.getServletPath(), null);

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(RoleNotFoundException e, HttpServletRequest request) {

        ErrorResponse response = buildErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage(),
                request.getServletPath(), null);

        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
