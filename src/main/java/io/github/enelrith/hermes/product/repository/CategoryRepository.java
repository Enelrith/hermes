package io.github.enelrith.hermes.product.repository;

import io.github.enelrith.hermes.product.dto.AddCategoryRequest;
import io.github.enelrith.hermes.product.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    boolean existsByName(String name);
}