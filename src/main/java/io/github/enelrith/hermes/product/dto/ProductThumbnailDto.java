package io.github.enelrith.hermes.product.dto;

import java.math.BigDecimal;

public record ProductThumbnailDto(Long id, String name, BigDecimal netPrice){
}