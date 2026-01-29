package io.github.enelrith.hermes.product.repository;

import io.github.enelrith.hermes.product.dto.projection.ProductDescription;
import io.github.enelrith.hermes.product.dto.projection.ProductSummary;
import io.github.enelrith.hermes.product.dto.projection.ProductThumbnail;
import io.github.enelrith.hermes.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<ProductSummary> findSummaryById(Long id);

    Optional<ProductDescription> findDescriptionById(Long id);

    List<ProductThumbnail> findAllByNameContainingIgnoreCase(String name);

    Optional<Product> findProductById(Long id);

    boolean existsByName(String name);
}