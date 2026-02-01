package io.github.enelrith.hermes.cart.entity;

import io.github.enelrith.hermes.common.enums.CartStatus;
import io.github.enelrith.hermes.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(value = AuditingEntityListener.class)
@Table(name = "carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    @LastModifiedDate
    private Instant updatedAt;

    @Column(name = "status", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private CartStatus status;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "cart", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.PERSIST)
    private Set<CartItem> cartItems = new HashSet<>();

    public CartItem findCartItemByProductId(Long productId) {
        var cartItems = this.getCartItems();

        for (CartItem cartItem : cartItems) {
            if (cartItem.getProduct().getId().equals(productId)) {
                return cartItem;
            }
        }
        return null;
    }

    public BigDecimal calculateCartGrossPrice(BigDecimal currentVat) {
        var cartItems = this.getCartItems();
        BigDecimal cartNetPrice = BigDecimal.ZERO;

        for (var cartItem : cartItems) {
            cartNetPrice = cartNetPrice.add(cartItem.getTotalNetPrice());
        }
        return cartNetPrice.add(cartNetPrice.multiply(currentVat)).setScale(2, RoundingMode.HALF_UP);
    }
}
