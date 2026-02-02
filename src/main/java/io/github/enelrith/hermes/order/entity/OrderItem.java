package io.github.enelrith.hermes.order.entity;

import io.github.enelrith.hermes.product.entity.Product;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "order_items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(name = "quantity", nullable = false, updatable = false)
    private Integer quantity;

    @Column(name = "unit_net_price", nullable = false, updatable = false, precision = 10, scale = 2)
    private BigDecimal unitNetPrice;

    @Column(name = "unit_shipping_net_price", nullable = false, updatable = false, precision = 10, scale = 2)
    private BigDecimal unitShippingPrice;

    @Column(name = "tax_rate", nullable = false, updatable = false, precision = 6, scale = 4)
    private BigDecimal taxRate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false, updatable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false, updatable = false)
    private Order order;

    @Transient
    public BigDecimal getUnitProductGrossPrice() {
        if (unitNetPrice == null || taxRate == null) return BigDecimal.ZERO;

        return unitNetPrice.add(unitNetPrice.multiply(taxRate)).setScale(2, RoundingMode.HALF_UP);
    }

    @Transient
    public BigDecimal getTotalProductGrossPrice() {
        if (quantity == null) return BigDecimal.ZERO;
        return getUnitProductGrossPrice().multiply(BigDecimal.valueOf(quantity)).setScale(2, RoundingMode.HALF_UP);
    }

    @Transient
    private final BigDecimal flatGrossShippingPrice = new BigDecimal("3.00");
    /**
     * Calculates the net price from the €3.00 flat gross rate.
     * Formula: net = gross / (1 + taxRate)
     */
    @Transient
    public BigDecimal getUnitShippingNetPrice() {
        // divisor = 1 + 0.24 (for example) = 1.24
        BigDecimal divisor = BigDecimal.ONE.add(taxRate);

        return flatGrossShippingPrice.divide(divisor, 2, RoundingMode.HALF_UP);
    }

    @Transient
    public BigDecimal getTotalShippingGrossPrice() {
        return flatGrossShippingPrice.multiply(BigDecimal.valueOf(quantity)).setScale(2, RoundingMode.HALF_UP);
    }
}
