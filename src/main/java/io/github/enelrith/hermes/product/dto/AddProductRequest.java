package io.github.enelrith.hermes.product.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record AddProductRequest(@Size(message = "The product name must be between {min} and {max} characters long", min = 1, max = 255)
                                @NotBlank(message = "The product name cannot be empty")
                                String name,
                                @Size(message = "The short description must be between {min} and {max} characters long", min = 1, max = 255)
                                String shortDescription,
                                @Size(message = "The long description must be between {min} and {max} characters long", min = 1, max = 10000)
                                String longDescription,
                                @NotNull(message = "The price cannot be null")
                                @Digits(message = "The price can contain a maximum of {integer} digits and {fraction} decimals", integer = 8, fraction = 2)
                                @PositiveOrZero(message = "The price must be a positive number or zero")
                                BigDecimal netPrice,
                                @NotNull(message = "The stock quantity cannot be null")
                                @Max(message = "The stock quantity cannot exceed 99 999", value = 99999)
                                @PositiveOrZero(message = "The stock quantity must be either positive or zero")
                                Integer stockQuantity,
                                @NotNull(message = "Product availability cannot be null")
                                Boolean isAvailable,
                                @NotBlank(message = "The category name cannot be empty")
                                @Size(min = 1, max = 50, message = "The category name must be between {min} and {max} characters long")
                                String categoryName,
                                @NotBlank(message = "The manufacturer name cannot be empty")
                                @Size(min = 1, max = 255, message = "The category name must be between {min} and {max} characters long")
                                String manufacturerName){
}