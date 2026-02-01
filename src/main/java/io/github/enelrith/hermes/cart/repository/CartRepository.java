package io.github.enelrith.hermes.cart.repository;

import io.github.enelrith.hermes.cart.entity.Cart;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    @EntityGraph(attributePaths = {"cartItems", "cartItems.product"})
    Optional<Cart> findByUser_Id(Long userId);
}