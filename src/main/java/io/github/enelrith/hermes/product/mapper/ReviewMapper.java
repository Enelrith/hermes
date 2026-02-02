package io.github.enelrith.hermes.product.mapper;

import io.github.enelrith.hermes.product.dto.AddReviewRequest;
import io.github.enelrith.hermes.product.dto.ReviewDto;
import io.github.enelrith.hermes.product.entity.Review;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ReviewMapper {
    @Mapping(source = "productId", target = "product.id")
    Review toEntity(ReviewDto reviewDto);

    @Mapping(source = "product.id", target = "productId")
    ReviewDto toReviewDto(Review review);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "productId", target = "product.id")
    Review partialUpdate(ReviewDto reviewDto, @MappingTarget Review review);

    Review toEntity(AddReviewRequest addReviewRequest);

    AddReviewRequest toAddReviewRequestDto(Review review);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Review partialUpdate(AddReviewRequest addReviewRequest, @MappingTarget Review review);
}