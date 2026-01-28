package io.github.enelrith.hermes.product.mapper;

import io.github.enelrith.hermes.product.dto.AddCategoryRequest;
import io.github.enelrith.hermes.product.dto.CategoryDto;
import io.github.enelrith.hermes.product.entity.Category;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface CategoryMapper {
    Category toEntity(AddCategoryRequest addCategoryRequest);

    CategoryDto toCategoryDto(Category category);
}