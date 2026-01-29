package io.github.enelrith.hermes.product.dto;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * DTO for {@link io.github.enelrith.hermes.product.entity.Product}
 */
public record ProductDescriptionDto(Long id, String name, String longDescription, BigDecimal netPrice,
                                    Boolean isAvailable, Instant createdAt) {
}