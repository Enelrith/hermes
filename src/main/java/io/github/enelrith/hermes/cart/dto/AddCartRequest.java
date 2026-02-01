package io.github.enelrith.hermes.cart.dto;

import io.github.enelrith.hermes.common.enums.CartStatus;
import jakarta.validation.constraints.NotNull;

public record AddCartRequest(@NotNull(message = "Cart status cannot be empty")
                             CartStatus status){
}