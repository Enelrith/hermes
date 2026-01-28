package io.github.enelrith.hermes.product.exception;

public class CategoryAlreadyExistsException extends RuntimeException {
    public CategoryAlreadyExistsException() {
        super("This category already exists");
    }

    public CategoryAlreadyExistsException(String message) {
        super(message);
    }
}
