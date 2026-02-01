package io.github.enelrith.hermes.cart.controller;

import io.github.enelrith.hermes.cart.dto.AddCartItemRequest;
import io.github.enelrith.hermes.cart.dto.CartDto;
import io.github.enelrith.hermes.cart.service.CartService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
@Validated
public class CartController {
    private final CartService cartService;

    @PostMapping
    public ResponseEntity<CartDto> addCart() {
        var cartDto = cartService.addCart();
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(cartDto.id())
                .toUri();
        return ResponseEntity.created(location).body(cartDto);
    }

    @PostMapping("/cart-items/{productId}")
    public ResponseEntity<CartDto> addCartItem(@PathVariable @Positive Long productId,
                                               @RequestBody @Valid AddCartItemRequest request) {
        var cartDto = cartService.addCartItem(productId, request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(cartDto.id())
                .toUri();
        return ResponseEntity.created(location).body(cartDto);
    }

    @PatchMapping("/cart-items/{cartItemId}")
    public ResponseEntity<CartDto> updateCartItem(@PathVariable @Positive Long cartItemId,
                                                  @RequestBody @Valid AddCartItemRequest request) {
        var cartDto = cartService.updateCartItem(cartItemId, request);
        return ResponseEntity.ok().body(cartDto);
    }

    @GetMapping
    public ResponseEntity<CartDto> getCart() {
        var cartDto = cartService.getCart();

        return ResponseEntity.ok().body(cartDto);
    }
}
