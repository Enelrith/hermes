package io.github.enelrith.hermes.order.service;

import io.github.enelrith.hermes.cart.entity.Cart;
import io.github.enelrith.hermes.cart.exception.CartDoesNotExistException;
import io.github.enelrith.hermes.cart.repository.CartRepository;
import io.github.enelrith.hermes.common.enums.OrderStatus;
import io.github.enelrith.hermes.order.dto.AddOrderRequest;
import io.github.enelrith.hermes.order.dto.OrderDto;
import io.github.enelrith.hermes.order.entity.Order;
import io.github.enelrith.hermes.order.entity.OrderItem;
import io.github.enelrith.hermes.order.mapper.OrderMapper;
import io.github.enelrith.hermes.order.repository.OrderRepository;
import io.github.enelrith.hermes.security.service.AuthService;
import io.github.enelrith.hermes.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final OrderMapper orderMapper;
    private final AuthService authService;

    @Transactional
    public OrderDto addOrder(AddOrderRequest request) {
        var user = authService.getCurrentUser();
        var cart = cartRepository.findByUser_Id(user.getId()).orElseThrow(CartDoesNotExistException::new);
        if (cart.getCartItems().isEmpty()) throw new CartDoesNotExistException("The cart is empty");
        var order = buildOrderEntity(request, cart, user);

        orderRepository.saveAndFlush(order);

        return orderMapper.toOrderDto(order);
    }

    public Set<OrderDto> getAllOrders() {
        var user = authService.getCurrentUser();
        var orders = orderRepository.findAllByUser_Id(user.getId());

        return orders.stream().map(orderMapper::toOrderDto).collect(Collectors.toSet());
    }

    private Order buildOrderEntity(AddOrderRequest request, Cart cart, User user) {
        Order order = new Order();
        Set<OrderItem> orderItems = cart.getCartItems().stream()
                .map(cartItem -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setProduct(cartItem.getProduct());
                    orderItem.setQuantity(cartItem.getQuantity());
                    orderItem.setUnitNetPrice(cartItem.getProduct().getNetPrice());
                    orderItem.setTaxRate(cartItem.getProduct().getVatRate());
                    orderItem.setUnitShippingPrice(orderItem.getUnitShippingNetPrice());
                    orderItem.setOrder(order);
                    return orderItem;
                })
                .collect(Collectors.toSet());
        order.setOrderItems(orderItems);
        order.setStatus(OrderStatus.PENDING);
        order.setNumber(generateOrderNumber());
        order.setDeliveryStreet(request.deliveryStreet());
        order.setDeliveryCity(request.deliveryCity());
        order.setDeliveryCountry(request.deliveryCountry());
        order.setDeliveryStateProvince(request.deliveryStateProvince());
        order.setDeliveryPostalCode(request.deliveryPostalCode());
        order.setPaymentMethod(request.paymentMethod());
        order.setUser(user);

        return order;
    }

    private String generateOrderNumber() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                + "-" + String.format("%05d", new Random().nextInt(100000));
    }
}
