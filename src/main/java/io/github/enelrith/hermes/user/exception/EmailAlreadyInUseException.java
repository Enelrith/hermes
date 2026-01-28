package io.github.enelrith.hermes.user.exception;

public class EmailAlreadyInUseException extends RuntimeException {
    public EmailAlreadyInUseException() {
        super("This email is already in use by a user");
    }

    public EmailAlreadyInUseException(String message) {
        super(message);
    }
}
