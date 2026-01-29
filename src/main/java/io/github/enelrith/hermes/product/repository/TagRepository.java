package io.github.enelrith.hermes.product.repository;

import io.github.enelrith.hermes.product.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Integer> {
    boolean existsByName(String name);
}