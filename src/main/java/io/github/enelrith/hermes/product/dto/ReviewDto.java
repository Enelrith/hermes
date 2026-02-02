package io.github.enelrith.hermes.product.dto;

public record ReviewDto(Long id, Integer rating, String description, Long productId){
}