package io.github.enelrith.hermes.product.repository;

import io.github.enelrith.hermes.product.entity.Manufacturer;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ManufacturerRepository extends JpaRepository<Manufacturer, Long> {
    boolean existsByName(String name);

    Optional<Manufacturer> findByName(String name);
}