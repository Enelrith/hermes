package io.github.enelrith.hermes.product.entity;

import io.github.enelrith.hermes.cart.entity.CartItem;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Formula;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 255)
    private String name;

    @Column(name = "short_description", nullable = true, length = 255)
    private String shortDescription;

    @Column(name = "long_description", columnDefinition = "TEXT", nullable = true)
    private String longDescription;

    @Column(name = "net_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal netPrice;

    @Column(name = "stock_quantity", nullable = false)
    private Integer stockQuantity;

    @Column(name = "is_available", nullable = false)
    private Boolean isAvailable;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    @LastModifiedDate
    private Instant updatedAt;

    @Column(name = "deleted_at", nullable = true)
    private Instant deletedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manufacturer_id", nullable = false)
    private Manufacturer manufacturer;

    @Column(name = "vat_rate", nullable = false, precision = 6, scale = 4)
    private BigDecimal vatRate;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "product_tags",
            joinColumns = @JoinColumn(name = "product_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "tag_id", nullable = false)
    )
    private Set<Tag> tags;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private Set<Review> reviews;

    @Transient
    public Double getAverageRating() {
        return reviews.stream().mapToInt(Review::getRating).average().orElse(0.0);
    }

    @Formula("net_price + (net_price * vat_rate)")
    private BigDecimal grossPrice;

    @Transient
    public BigDecimal getGrossPrice() {
        return netPrice.add(netPrice.multiply(vatRate)).setScale(2, RoundingMode.HALF_UP);
    }
}
