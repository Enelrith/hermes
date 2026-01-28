package io.github.enelrith.hermes.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AddManufacturerRequest(@NotBlank(message = "The manufacturer name cannot be empty")
                                     @Size(min = 1, max = 255, message = "The manufacturer name can be between {min} and {max} characters long")
                                     String name) {
}
