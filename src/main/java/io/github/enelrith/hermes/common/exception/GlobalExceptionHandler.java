package io.github.enelrith.hermes.common.exception;

import io.github.enelrith.hermes.cart.exception.CartAlreadyExistsException;
import io.github.enelrith.hermes.cart.exception.CartDoesNotExistException;
import io.github.enelrith.hermes.cart.exception.InvalidCartItemException;
import io.github.enelrith.hermes.product.exception.*;
import io.github.enelrith.hermes.security.exception.InvalidRefreshTokenException;
import io.github.enelrith.hermes.security.exception.InvalidUserException;
import io.github.enelrith.hermes.user.exception.EmailAlreadyInUseException;
import io.github.enelrith.hermes.user.exception.PasswordsDoNotMatchException;
import io.github.enelrith.hermes.user.exception.RoleNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

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

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleException(ConstraintViolationException e, HttpServletRequest request) {
        Map<String, String> errors = new HashMap<>();
        e.getConstraintViolations().forEach(error ->
                errors.put(error.getPropertyPath().toString(), error.getMessage()));

        ErrorResponse response = buildErrorResponse(HttpStatus.BAD_REQUEST, "Invalid path variable",
                request.getServletPath(), errors);

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleException(AuthenticationException e, HttpServletRequest request) {

        ErrorResponse response = buildErrorResponse(HttpStatus.UNAUTHORIZED, "Invalid email or password",
                request.getServletPath(), null);

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleException(MethodArgumentTypeMismatchException e, HttpServletRequest request) {

        String parameterName = e.getName();
        String providedType = e.getValue() != null ? e.getValue().getClass().getSimpleName() : null;
        String requiredType = e.getRequiredType() != null ? e.getRequiredType().getSimpleName() : null;
        String message = "Parameter " + parameterName + " expects a " +  requiredType + " but the provided value is a " + providedType;

        ErrorResponse response = buildErrorResponse(HttpStatus.BAD_REQUEST, message,
                request.getServletPath(), null);

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ErrorResponse> handleException(AuthorizationDeniedException e, HttpServletRequest request) {

        ErrorResponse response = buildErrorResponse(HttpStatus.UNAUTHORIZED, "You are not authorized to perform this operation",
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

    @ExceptionHandler(CategoryAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleException(CategoryAlreadyExistsException e, HttpServletRequest request) {

        ErrorResponse response = buildErrorResponse(HttpStatus.CONFLICT, e.getMessage(),
                request.getServletPath(), null);

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @ExceptionHandler(CategoryDoesNotExistException.class)
    public ResponseEntity<ErrorResponse> handleException(CategoryDoesNotExistException e, HttpServletRequest request) {

        ErrorResponse response = buildErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage(),
                request.getServletPath(), null);

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @ExceptionHandler(ManufacturerAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleException(ManufacturerAlreadyExistsException e, HttpServletRequest request) {

        ErrorResponse response = buildErrorResponse(HttpStatus.CONFLICT, e.getMessage(),
                request.getServletPath(), null);

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @ExceptionHandler(ManufacturerDoesNotExistException.class)
    public ResponseEntity<ErrorResponse> handleException(ManufacturerDoesNotExistException e, HttpServletRequest request) {

        ErrorResponse response = buildErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage(),
                request.getServletPath(), null);

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @ExceptionHandler(ProductAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleException(ProductAlreadyExistsException e, HttpServletRequest request) {

        ErrorResponse response = buildErrorResponse(HttpStatus.CONFLICT, e.getMessage(),
                request.getServletPath(), null);

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @ExceptionHandler(ProductDoesNotExistException.class)
    public ResponseEntity<ErrorResponse> handleException(ProductDoesNotExistException e, HttpServletRequest request) {

        ErrorResponse response = buildErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage(),
                request.getServletPath(), null);

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @ExceptionHandler(TagAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleException(TagAlreadyExistsException e, HttpServletRequest request) {

        ErrorResponse response = buildErrorResponse(HttpStatus.CONFLICT, e.getMessage(),
                request.getServletPath(), null);

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @ExceptionHandler(TagDoesNotExistException.class)
    public ResponseEntity<ErrorResponse> handleException(TagDoesNotExistException e, HttpServletRequest request) {

        ErrorResponse response = buildErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage(),
                request.getServletPath(), null);

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @ExceptionHandler(CartDoesNotExistException.class)
    public ResponseEntity<ErrorResponse> handleException(CartDoesNotExistException e, HttpServletRequest request) {

        ErrorResponse response = buildErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage(),
                request.getServletPath(), null);

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @ExceptionHandler(InvalidCartItemException.class)
    public ResponseEntity<ErrorResponse> handleException(InvalidCartItemException e, HttpServletRequest request) {

        ErrorResponse response = buildErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage(),
                request.getServletPath(), null);

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @ExceptionHandler(CartAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleException(CartAlreadyExistsException e, HttpServletRequest request) {

        ErrorResponse response = buildErrorResponse(HttpStatus.CONFLICT, e.getMessage(),
                request.getServletPath(), null);

        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
