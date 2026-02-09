package io.github.enelrith.hermes.product.mapper;

import io.github.enelrith.hermes.product.dto.*;
import io.github.enelrith.hermes.product.entity.Product;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {
        CategoryMapper.class, ManufacturerMapper.class, TagMapper.class
})
public interface ProductMapper {
    // -- PRODUCT SUMMARY --
    Product toEntity(ProductSummaryDto productSummaryDto);

    // -- PRODUCT DESCRIPTION --
    Product toEntity(ProductDescriptionDto productDescriptionDto);

    // -- PRODUCT THUMBNAIL --
    Product toEntity(ProductThumbnailDto productThumbnailDto);

    ProductThumbnailDto toProductThumbnailDto(Product product);
    // -- ADD PRODUCT REQUEST --
    Product toEntity(AddProductRequest addProductRequest);

    // -- PRODUCT FULL --
    Product toEntity(ProductFullDto productFullDto);

    ProductFullDto toProductFullDto(Product product);
}