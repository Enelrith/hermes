package io.github.enelrith.hermes.product.dto;

import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record ProductFilters(String name, @PositiveOrZero BigDecimal minPrice, @PositiveOrZero BigDecimal maxPrice) {
}
