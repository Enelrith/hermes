package io.github.enelrith.hermes.cart.exception;

public class InvalidCartItemException extends RuntimeException {
    public InvalidCartItemException() {
        super("The cart does not contain this item");
    }

    public InvalidCartItemException(String message) {
        super(message);
    }
}
