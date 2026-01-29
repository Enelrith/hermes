package io.github.enelrith.hermes.product.dto;

import java.util.List;

public record AddTagToProductResponse(Long id, List<TagDto> tags) {
}
