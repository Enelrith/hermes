package io.github.enelrith.hermes.product.exception;

public class ProductDoesNotExistException extends RuntimeException {
    public ProductDoesNotExistException() {
        super("This product does not exist");
    }

    public ProductDoesNotExistException(String message) {
        super(message);
    }
}
