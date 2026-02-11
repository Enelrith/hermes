package io.github.enelrith.hermes.product.specification;

import io.github.enelrith.hermes.product.entity.Product;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class ProductSpecifications {
    public static Specification<Product> containsName(String name) {
        return (root, query, builder) -> name == null ? null : builder.like(builder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Product> hasMinPrice(BigDecimal minPrice) {
        return (root, query,builder) -> minPrice == null ? null : builder.greaterThanOrEqualTo(root.get("grossPrice"), minPrice);
    }

    public static Specification<Product> hasMaxPrice(BigDecimal maxPrice) {
        return (root, query, builder) -> maxPrice == null ? null : builder.lessThanOrEqualTo(root.get("grossPrice"), maxPrice);
    }
}
