package io.github.enelrith.hermes.cart.mapper;

import io.github.enelrith.hermes.cart.dto.AddCartRequest;
import io.github.enelrith.hermes.cart.dto.CartDto;
import io.github.enelrith.hermes.cart.entity.Cart;
import org.mapstruct.*;

import java.math.BigDecimal;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING,
        uses = CartItemMapper.class)
public interface CartMapper {
    Cart toEntity(CartDto cartDto);

    CartDto toCartDto(Cart cart);

    Cart toEntity(AddCartRequest addCartRequest);
}