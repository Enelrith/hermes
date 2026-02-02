package io.github.enelrith.hermes.order.mapper;

import io.github.enelrith.hermes.order.dto.OrderDto;
import io.github.enelrith.hermes.order.entity.Order;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING,
        uses = OrderItemMapper.class)
public interface OrderMapper {
    @Mapping(source = "userId", target = "user.id")
    Order toEntity(OrderDto orderDto);

    @AfterMapping
    default void linkOrderItems(@MappingTarget Order order) {
        order.getOrderItems().forEach(orderItem -> orderItem.setOrder(order));
    }

    @Mapping(source = "user.id", target = "userId")
    OrderDto toOrderDto(Order order);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "userId", target = "user.id")
    Order partialUpdate(OrderDto orderDto, @MappingTarget Order order);
}