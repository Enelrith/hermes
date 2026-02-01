package io.github.enelrith.hermes.cart.exception;

public class CartAlreadyExistsException extends RuntimeException {
    public CartAlreadyExistsException() {
        super("A cart already exists for this user");
    }

    public CartAlreadyExistsException(String message) {
        super(message);
    }
}
