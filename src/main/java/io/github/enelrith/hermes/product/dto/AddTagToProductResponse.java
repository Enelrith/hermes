package io.github.enelrith.hermes.product.dto;

import java.util.Set;

public record AddTagToProductResponse(Long id, Set<TagDto> tags) {
}
