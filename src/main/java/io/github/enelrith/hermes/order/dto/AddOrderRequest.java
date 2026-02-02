package io.github.enelrith.hermes.order.dto;

import io.github.enelrith.hermes.common.enums.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AddOrderRequest(
        @Size(message = "Street must be between {min} and {max} characters long", min = 1, max = 100)
        @NotBlank(message = "Street cannot be empty")
        String deliveryStreet,
        @Size(message = "City must be between {min} and {max} characters long", min = 1, max = 100)
        @NotBlank(message = "City cannot be empty")
        String deliveryCity,
        @Size(message = "State or province must be between {min} and {max} characters long", min = 1, max = 100)
        @NotBlank(message = "State or province cannot be empty")
        String deliveryStateProvince,
        @Size(message = "Country must be between {min} and {max} characters long", min = 1, max = 100)
        @NotBlank(message = "Country cannot be empty")
        String deliveryCountry,
        @Size(message = "Postal code can be between {min} and {max} characters long", min = 1, max = 100)
        @NotBlank(message = "Postal code cannot be empty")
        String deliveryPostalCode,
        @NotNull(message = "Payment method cannot be empty")
        PaymentMethod paymentMethod){
}