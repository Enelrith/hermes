package io.github.enelrith.hermes.product.exception;

public class ManufacturerDoesNotExistException extends RuntimeException {
    public ManufacturerDoesNotExistException() {
        super("This manufacturer does not exist");
    }

    public ManufacturerDoesNotExistException(String message) {
        super(message);
    }
}
