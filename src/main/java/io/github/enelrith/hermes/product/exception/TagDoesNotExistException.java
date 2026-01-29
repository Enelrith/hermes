package io.github.enelrith.hermes.product.exception;

public class TagDoesNotExistException extends RuntimeException {
    public TagDoesNotExistException() {
        super("This tag does not exist");
    }

    public TagDoesNotExistException(String message) {
        super(message);
    }
}
