package io.github.enelrith.hermes.product.exception;

public class ProductAlreadyExistsException extends RuntimeException {
    public ProductAlreadyExistsException() {
        super("This product already exists");
    }

    public ProductAlreadyExistsException(String message) {
        super(message);
    }
}
