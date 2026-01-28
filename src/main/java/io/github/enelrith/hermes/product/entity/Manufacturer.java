package io.github.enelrith.hermes.product.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "manufacturers")
public class Manufacturer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 255)
    private String name;

    @OneToMany(mappedBy = "manufacturer", fetch = FetchType.LAZY)
    private Set<Product> products;
}
