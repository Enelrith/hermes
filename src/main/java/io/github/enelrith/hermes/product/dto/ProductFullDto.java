package io.github.enelrith.hermes.product.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record ProductFullDto(Long id, String name, String shortDescription, String longDescription, BigDecimal netPrice,
                             Integer stockQuantity, Boolean isAvailable, Instant createdAt, Instant updatedAt,
                             Instant deletedAt, CategoryDto category, ManufacturerDto manufacturer) {
}