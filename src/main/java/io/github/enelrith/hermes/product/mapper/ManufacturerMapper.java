package io.github.enelrith.hermes.product.mapper;

import io.github.enelrith.hermes.product.dto.AddManufacturerRequest;
import io.github.enelrith.hermes.product.dto.ManufacturerDto;
import io.github.enelrith.hermes.product.entity.Manufacturer;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ManufacturerMapper {
    ManufacturerDto toManufacturerDto(Manufacturer manufacturer);

    Manufacturer toEntity(AddManufacturerRequest request);
}