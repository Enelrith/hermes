package io.github.enelrith.hermes.product.exception;

public class CategoryDoesNotExistException extends RuntimeException {
    public CategoryDoesNotExistException() {
        super("This category does not exist");
    }

    public CategoryDoesNotExistException(String message) {
        super(message);
    }
}
