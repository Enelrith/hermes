package io.github.enelrith.hermes.product.repository;

import io.github.enelrith.hermes.product.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface TagRepository extends JpaRepository<Tag, Integer> {
    boolean existsByName(String name);

    Set<Tag> findByProducts_Id(Long productId);
}