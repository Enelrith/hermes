package io.github.enelrith.hermes.order.dto;

import io.github.enelrith.hermes.common.enums.OrderStatus;
import io.github.enelrith.hermes.common.enums.PaymentMethod;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

public record OrderDto(Long id, String number, OrderStatus status, PaymentMethod paymentMethod, Instant createdAt,
                       Instant shippedAt, Instant deliveredAt, Long userId, Set<OrderItemDto> orderItems,
                       BigDecimal orderProductGrossPrice, BigDecimal orderShippingGrossPrice,
                       String fullDeliveryAddress, String successUrl, String cancelUrl){
}