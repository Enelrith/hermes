package io.github.enelrith.hermes.product.dto.projection;

import java.math.BigDecimal;
import java.time.Instant;

public interface ProductDescription {
    Long getId();
    String getName();
    String getLongDescription();
    BigDecimal getNetPrice();
    Boolean getIsAvailable();
    Instant getCreatedAt();
}
