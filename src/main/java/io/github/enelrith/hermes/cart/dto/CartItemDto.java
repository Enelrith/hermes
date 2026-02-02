package io.github.enelrith.hermes.cart.dto;

import io.github.enelrith.hermes.product.dto.ProductThumbnailDto;

import java.math.BigDecimal;

public record CartItemDto(Long id, Integer quantity, BigDecimal totalGrossPrice,
                          ProductThumbnailDto product){
}