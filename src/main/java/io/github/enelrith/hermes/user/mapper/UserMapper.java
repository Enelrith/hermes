package io.github.enelrith.hermes.user.mapper;

import io.github.enelrith.hermes.user.dto.RegisterUserRequest;
import io.github.enelrith.hermes.user.entity.User;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    User toEntity(RegisterUserRequest registerUserRequest);

    RegisterUserRequest toDto(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User partialUpdate(RegisterUserRequest registerUserRequest, @MappingTarget User user);
}