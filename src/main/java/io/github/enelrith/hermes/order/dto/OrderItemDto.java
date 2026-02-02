package io.github.enelrith.hermes.order.dto;

import io.github.enelrith.hermes.product.dto.ProductThumbnailDto;

import java.math.BigDecimal;

public record OrderItemDto(Long id, Integer quantity, BigDecimal unitNetPrice, BigDecimal unitShippingPrice,
                           BigDecimal taxRate, ProductThumbnailDto product, BigDecimal unitProductGrossPrice,
                           BigDecimal totalProductGrossPrice) {
}