package io.github.enelrith.hermes.order.controller;

import com.stripe.exception.StripeException;
import io.github.enelrith.hermes.order.dto.AddOrderRequest;
import io.github.enelrith.hermes.order.dto.OrderDto;
import io.github.enelrith.hermes.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Set;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Validated
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDto> addOrder(@Valid @RequestBody AddOrderRequest request) {
        try {
            var orderDto = orderService.addOrder(request);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(orderDto.id())
                    .toUri();
            return ResponseEntity.created(location).body(orderDto);
        } catch (StripeException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping
    public ResponseEntity<Set<OrderDto>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }
}
