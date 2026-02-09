package io.github.enelrith.hermes.product.dto;

import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

/**
 * DTO for {@link io.github.enelrith.hermes.product.entity.Product}
 */
public record ProductDescriptionDto(Long id, String name, String longDescription, BigDecimal grossPrice,
                                    Boolean isAvailable, Instant createdAt) {
}