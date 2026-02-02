package io.github.enelrith.hermes.product.repository;

import io.github.enelrith.hermes.product.dto.ProductDescriptionDto;
import io.github.enelrith.hermes.product.dto.ProductSummaryDto;
import io.github.enelrith.hermes.product.dto.ProductThumbnailDto;
import io.github.enelrith.hermes.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.Set;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("select new io.github.enelrith.hermes.product.dto.ProductSummaryDto(p.id, p.name, p.shortDescription, cast(p.netPrice + (p.netPrice * p.vatRate) as bigdecimal)," +
            " p.isAvailable, p.createdAt) from Product p where p.id=:id")
    Optional<ProductSummaryDto> findSummaryById(Long id);

    @Query("select new io.github.enelrith.hermes.product.dto.ProductDescriptionDto(p.id, p.name, p.longDescription, cast(p.netPrice + (p.netPrice * p.vatRate) as bigdecimal)," +
            " p.isAvailable, p.createdAt) from Product p where p.id=:id")
    Optional<ProductDescriptionDto> findDescriptionById(@Param("id") Long id);

    @Query("select new io.github.enelrith.hermes.product.dto.ProductThumbnailDto(" +
            "p.id, p.name, cast(p.netPrice + (p.netPrice * p.vatRate) as bigdecimal)) " +
            "from Product p " +
            "where lower(p.name) like lower(concat('%', :name, '%'))")
    Set<ProductThumbnailDto> findAllByNameContainingIgnoreCase(@Param("name") String name);

    boolean existsByName(String name);
}