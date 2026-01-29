package io.github.enelrith.hermes.product.mapper;

import io.github.enelrith.hermes.product.dto.AddTagRequest;
import io.github.enelrith.hermes.product.dto.TagDto;
import io.github.enelrith.hermes.product.entity.Tag;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface TagMapper {
    Tag toEntity(TagDto tagDto);

    TagDto toTagDto(Tag tag);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Tag partialUpdate(TagDto tagDto, @MappingTarget Tag tag);

    Tag toEntity(AddTagRequest addTagRequest);

    AddTagRequest toDto1(Tag tag);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Tag partialUpdate(AddTagRequest addTagRequest, @MappingTarget Tag tag);
}