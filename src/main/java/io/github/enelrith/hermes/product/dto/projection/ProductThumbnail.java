package io.github.enelrith.hermes.product.dto.projection;

import java.math.BigDecimal;

public interface ProductThumbnail {
    Long getId();
    String getName();
    BigDecimal getNetPrice();
}
