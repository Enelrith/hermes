package io.github.enelrith.hermes.product.exception;

public class ManufacturerAlreadyExistsException extends RuntimeException {
    public ManufacturerAlreadyExistsException() {
        super("This manufacturer already exists");
    }

    public ManufacturerAlreadyExistsException(String message) {
        super(message);
    }
}
