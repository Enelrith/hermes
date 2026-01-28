package io.github.enelrith.hermes.security.exception;

public class InvalidUserException extends RuntimeException {
    public InvalidUserException() {
        super("Invalid user");
    }

    public InvalidUserException(String message) {
        super(message);
    }
}
