package io.github.enelrith.hermes.cart.dto;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;

public record AddCartItemRequest(
        @NotNull(message = "Quantity cannot be null")
        @Range(message = "Quantity must be between {min} and {max}", min = -99, max = 99)
        Integer quantity){
}