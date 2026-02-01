package io.github.enelrith.hermes.cart.exception;

public class CartDoesNotExistException extends RuntimeException {
    public CartDoesNotExistException() {
        super("This cart does not exist");
    }

    public CartDoesNotExistException(String message) {
        super(message);
    }
}
