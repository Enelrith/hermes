package io.github.enelrith.hermes.cart.service;

import io.github.enelrith.hermes.cart.dto.AddCartItemRequest;
import io.github.enelrith.hermes.cart.dto.AddCartRequest;
import io.github.enelrith.hermes.cart.dto.CartDto;
import io.github.enelrith.hermes.cart.dto.CartItemDto;
import io.github.enelrith.hermes.cart.entity.Cart;
import io.github.enelrith.hermes.cart.entity.CartItem;
import io.github.enelrith.hermes.cart.exception.CartDoesNotExistException;
import io.github.enelrith.hermes.cart.exception.InvalidCartItemException;
import io.github.enelrith.hermes.cart.mapper.CartMapper;
import io.github.enelrith.hermes.cart.repository.CartRepository;
import io.github.enelrith.hermes.common.enums.CartStatus;
import io.github.enelrith.hermes.product.dto.ProductThumbnailDto;
import io.github.enelrith.hermes.product.entity.Product;
import io.github.enelrith.hermes.product.exception.ProductDoesNotExistException;
import io.github.enelrith.hermes.product.repository.ProductRepository;
import io.github.enelrith.hermes.security.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final AuthService authService;
    private final ProductRepository productRepository;

    @Value("${spring.application.finance.current-vat}")
    private BigDecimal currentVat;

    @Transactional
    public CartDto addCart() {
        var currentUser = authService.getCurrentUser();

        var cart = cartMapper.toEntity(new AddCartRequest(CartStatus.ACTIVE));
        cart.setUser(currentUser);

        var savedCart = cartRepository.save(cart);

        return buildCartDto(savedCart);
    }

    @Transactional
    public CartDto addCartItem(Long productId, AddCartItemRequest request) {
        if (request.quantity() < 1) throw new InvalidCartItemException("Cart item quantity must be positive");
        var product = productRepository.findById(productId).orElseThrow(ProductDoesNotExistException::new);
        var user = authService.getCurrentUser();
        var cart = cartRepository.findByUser_Id(user.getId()).orElseThrow(CartDoesNotExistException::new);
        var cartItem = cart.findCartItemByProductId(productId);
        if (cartItem != null) throw new InvalidCartItemException("This cart already contains this item");

        cartItem = buildCartItem(request.quantity(), product);
        cartItem.setCart(cart);
        cart.getCartItems().add(cartItem);

        cartRepository.flush();

        return buildCartDto(cart);
    }

    @Transactional
    public CartDto updateCartItem(Long cartItemId, AddCartItemRequest request) {
        var user = authService.getCurrentUser();
        var cart = cartRepository.findByUser_Id(user.getId()).orElseThrow(CartDoesNotExistException::new);
        var cartItem = cart.getCartItems().stream().filter(item ->
                item.getId().equals(cartItemId)).findFirst().orElseThrow(InvalidCartItemException::new);

        if (cartItem.getQuantity() + request.quantity() <= 0){
            cart.getCartItems().remove(cartItem);
        } else {
            cartItem.setQuantity(cartItem.getQuantity() + request.quantity());
        }
        cartItem.setTotalNetPrice(cartItem.calculateTotalNetPrice());
        cart.calculateCartGrossPrice(cartItem.getTotalNetPrice());

        return buildCartDto(cart);
    }

    public CartDto getCart() {
        var user = authService.getCurrentUser();
        var cart = cartRepository.findByUser_Id(user.getId()).orElseThrow(CartDoesNotExistException::new);

        return buildCartDto(cart);
    }

    private CartItem buildCartItem(Integer quantity, Product product) {
        var cartItem = new CartItem();
        cartItem.setQuantity(quantity);
        cartItem.setProduct(product);
        cartItem.setTotalNetPrice(cartItem.calculateTotalNetPrice());

        return cartItem;
    }

    private CartDto buildCartDto(Cart cart) {
        Set<CartItemDto> cartItemDtoSet = new HashSet<>();

        for (var cartItem : cart.getCartItems()) {
            var cartItemDto = new CartItemDto(
                    cartItem.getId(),
                    cartItem.getQuantity(),
                    cartItem.getTotalNetPrice(),
                    new ProductThumbnailDto(
                            cartItem.getProduct().getId(),
                            cartItem.getProduct().getName(),
                            cartItem.getProduct().getNetPrice()
                    )
            );
            cartItemDtoSet.add(cartItemDto);
        }

        return CartDto.builder()
                .id(cart.getId())
                .createdAt(cart.getCreatedAt())
                .updatedAt(cart.getUpdatedAt())
                .status(cart.getStatus())
                .cartTotalPrice(cart.calculateCartGrossPrice(currentVat))
                .cartItems(cartItemDtoSet)
                .build();
    }
}
