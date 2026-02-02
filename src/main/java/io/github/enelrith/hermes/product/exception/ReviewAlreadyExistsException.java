package io.github.enelrith.hermes.product.exception;

public class ReviewAlreadyExistsException extends RuntimeException {
    public ReviewAlreadyExistsException() {
        super("This user has already reviewed this product");
    }

    public ReviewAlreadyExistsException(String message) {
        super(message);
    }
}
