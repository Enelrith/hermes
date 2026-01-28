package io.github.enelrith.hermes.user.repository;

import io.github.enelrith.hermes.user.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}