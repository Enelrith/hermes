package io.github.enelrith.hermes.product.mapper;

import io.github.enelrith.hermes.product.dto.*;
import io.github.enelrith.hermes.product.entity.Product;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductMapper {
    Product toEntity(ProductSummaryDto productSummaryDto);

    ProductSummaryDto toProductSummaryDto(Product product);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Product partialUpdate(ProductSummaryDto productSummaryDto, @MappingTarget Product product);

    Product toEntity(ProductDescriptionDto productDescriptionDto);

    ProductDescriptionDto toProductDescriptionDto(Product product);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Product partialUpdate(ProductDescriptionDto productDescriptionDto, @MappingTarget Product product);

    Product toEntity(ProductThumbnailDto productThumbnailDto);

    ProductThumbnailDto toProductThumbnailDto(Product product);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Product partialUpdate(ProductThumbnailDto productThumbnailDto, @MappingTarget Product product);

    Product toEntity(AddProductRequest addProductRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Product partialUpdate(AddProductRequest addProductRequest, @MappingTarget Product product);

    Product toEntity(ProductFullDto productFullDto);

    ProductFullDto toProductFullDto(Product product);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Product partialUpdate(ProductFullDto productFullDto, @MappingTarget Product product);
}