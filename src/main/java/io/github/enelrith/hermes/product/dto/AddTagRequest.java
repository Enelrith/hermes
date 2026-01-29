package io.github.enelrith.hermes.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AddTagRequest(
        @Size(message = "The tag name must be between {min} and {max} characters long", min = 1, max = 50)
        @NotBlank(message = "The tag name cannot be empty")
        String name){
}