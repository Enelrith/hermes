package io.github.enelrith.hermes.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AddCategoryRequest(@NotBlank(message = "Category name cannot be empty")
                                 @Size(min = 1, max = 50, message = "Category name must be between {min} and {max} characters long")
                                 String name) {
}
