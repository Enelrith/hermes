package io.github.enelrith.hermes.product.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record ProductSummaryDto(Long id, String name, String shortDescription, BigDecimal grossPrice, Boolean isAvailable,
                                Instant createdAt){
}