package io.github.enelrith.hermes.product.exception;

public class TagAlreadyExistsException extends RuntimeException {
    public TagAlreadyExistsException() {
        super("This tag already exists");
    }

    public TagAlreadyExistsException(String message) {
        super(message);
    }
}
