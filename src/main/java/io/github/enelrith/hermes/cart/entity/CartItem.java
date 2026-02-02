package io.github.enelrith.hermes.cart.entity;

import io.github.enelrith.hermes.product.entity.Product;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cart_items")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "total_net_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalNetPrice;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @Transient
    public BigDecimal calculateTotalNetPrice(){
        return product.getNetPrice().multiply(new BigDecimal(quantity)).setScale(2, RoundingMode.HALF_UP);
    }

    @Transient
    public BigDecimal getTotalGrossPrice() {
        var productGrossPrice = product.getGrossPrice();

        return productGrossPrice.multiply(new BigDecimal(quantity)).setScale(2, RoundingMode.HALF_UP);
    }
}
