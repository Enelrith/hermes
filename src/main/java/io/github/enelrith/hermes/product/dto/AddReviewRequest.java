package io.github.enelrith.hermes.product.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Range;

public record AddReviewRequest(
        @NotNull(message = "Rating cannot be empty")
        @Range(message = "Rating must be between {min} and {max}", min = 1, max = 5)
        Integer rating,
        @Size(message = "Description must be between {min} and {max} characters long", min = 1, max = 255)
        String description){
}