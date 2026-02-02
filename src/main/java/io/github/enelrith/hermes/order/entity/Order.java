package io.github.enelrith.hermes.order.entity;

import io.github.enelrith.hermes.common.enums.OrderStatus;
import io.github.enelrith.hermes.common.enums.PaymentMethod;
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
@EntityListeners(AuditingEntityListener.class)
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(name = "number", nullable = false, unique = true, length = 20, updatable = false)
    private String number;

    @Column(name = "status", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name = "delivery_street", nullable = false, length = 100, updatable = false)
    private String deliveryStreet;

    @Column(name = "delivery_city", nullable = false, length = 100, updatable = false)
    private String deliveryCity;

    @Column(name = "delivery_state_province", nullable = false, length = 100, updatable = false)
    private String deliveryStateProvince;

    @Column(name = "delivery_country", nullable = false, length = 100, updatable = false)
    private String deliveryCountry;

    @Column(name = "delivery_postal_code", nullable = false, length = 20, updatable = false)
    private String deliveryPostalCode;

    @Column(name = "payment_method", nullable = false, length = 20, updatable = false)
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    @LastModifiedDate
    private Instant updatedAt;

    @Column(name = "shipped_at", nullable = true, updatable = false)
    private Instant shippedAt;

    @Column(name = "delivered_at", nullable = true, updatable = false)
    private Instant deliveredAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private Set<OrderItem> orderItems = new HashSet<>();

    @Transient
    public BigDecimal getOrderProductGrossPrice() {
        if (orderItems.isEmpty()) return BigDecimal.ZERO;

        return orderItems.stream().map(OrderItem::getTotalProductGrossPrice).reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    @Transient
    public BigDecimal getOrderShippingGrossPrice() {
        if (orderItems.isEmpty()) return BigDecimal.ZERO;

        return orderItems.stream().map(OrderItem::getTotalShippingGrossPrice).reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    @Transient
    public String getFullDeliveryAddress() {
        return deliveryStreet + ", " + deliveryCity + ", " + deliveryStateProvince +
                ", " + deliveryCountry +  ", " + deliveryPostalCode;
    }
}
