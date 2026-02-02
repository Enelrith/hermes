package io.github.enelrith.hermes.cart.mapper;

import io.github.enelrith.hermes.cart.dto.CartItemDto;
import io.github.enelrith.hermes.cart.entity.CartItem;
import io.github.enelrith.hermes.product.mapper.ProductMapper;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING,
        uses = ProductMapper.class)
public interface CartItemMapper {
    CartItem toEntity(CartItemDto cartItemDto);

    CartItemDto toCartItemDto(CartItem cartItem);
}