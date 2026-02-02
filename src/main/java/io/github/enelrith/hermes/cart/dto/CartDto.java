package io.github.enelrith.hermes.cart.dto;

import io.github.enelrith.hermes.common.enums.CartStatus;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

@Builder
public record CartDto(Long id, Instant createdAt, Instant updatedAt, CartStatus status, BigDecimal cartGrossPrice,
                      Set<CartItemDto> cartItems){
}