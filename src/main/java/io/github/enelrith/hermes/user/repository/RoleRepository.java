package io.github.enelrith.hermes.user.repository;

import io.github.enelrith.hermes.common.enums.RoleName;
import io.github.enelrith.hermes.user.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Byte> {
    Optional<Role> findByName(RoleName name);
}