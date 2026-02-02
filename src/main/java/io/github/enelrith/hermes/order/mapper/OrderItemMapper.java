package io.github.enelrith.hermes.order.mapper;

import io.github.enelrith.hermes.order.dto.OrderItemDto;
import io.github.enelrith.hermes.order.entity.OrderItem;
import io.github.enelrith.hermes.product.mapper.ProductMapper;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING,
        uses = ProductMapper.class)
public interface OrderItemMapper {
    OrderItem toEntity(OrderItemDto orderItemDto);

    OrderItemDto toOrderItemDto(OrderItem orderItem);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    OrderItem partialUpdate(OrderItemDto orderItemDto, @MappingTarget OrderItem orderItem);
}