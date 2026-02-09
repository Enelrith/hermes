package io.github.enelrith.hermes.product.repository;

import io.github.enelrith.hermes.product.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Review> findByProduct_Id(Long id);

    Set<Review> findAllByUser_Id(Long userId);

    Set<Review> findAllByProduct_Id(Long productId);
}