package io.github.enelrith.hermes.order.repository;

import io.github.enelrith.hermes.order.entity.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @EntityGraph(attributePaths = {"orderItem", "orderItem.product"})
    Optional<Order> findByUser_Id(Long id);

     Set<Order> findAllByUser_Id(Long userId);
}