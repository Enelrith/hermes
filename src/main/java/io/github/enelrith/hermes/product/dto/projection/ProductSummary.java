package io.github.enelrith.hermes.product.dto.projection;

import java.math.BigDecimal;
import java.time.Instant;

public interface ProductSummary {
    Long getId();
    String getName();
    String getShortDescription();
    BigDecimal getNetPrice();
    Boolean getIsAvailable();
    Instant getCreatedAt();
}
