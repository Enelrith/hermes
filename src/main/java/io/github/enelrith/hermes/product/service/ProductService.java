package io.github.enelrith.hermes.product.service;

import io.github.enelrith.hermes.product.dto.*;
import io.github.enelrith.hermes.product.exception.*;
import io.github.enelrith.hermes.product.mapper.ProductMapper;
import io.github.enelrith.hermes.product.mapper.TagMapper;
import io.github.enelrith.hermes.product.repository.CategoryRepository;
import io.github.enelrith.hermes.product.repository.ManufacturerRepository;
import io.github.enelrith.hermes.product.repository.ProductRepository;
import io.github.enelrith.hermes.product.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;
    private final ManufacturerRepository manufacturerRepository;
    private final TagRepository tagRepository;

    private final BigDecimal currentVat = new BigDecimal("0.24");
    private final TagMapper tagMapper;

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ProductFullDto addProduct(AddProductRequest request) {
        if (productRepository.existsByName(request.name())) throw new ProductAlreadyExistsException();

        var category = categoryRepository.findByName(request.categoryName()).orElseThrow(CategoryDoesNotExistException::new);
        var manufacturer = manufacturerRepository.findByName(request.manufacturerName())
                .orElseThrow(ManufacturerDoesNotExistException::new);

        var product = productMapper.toEntity(request);
        product.setCategory(category);
        product.setManufacturer(manufacturer);
        productRepository.save(product);

        return productMapper.toProductFullDto(product);
    }

    public ProductSummaryDto getProductSummaryById(Long id) {
        var productSummary = productRepository.findSummaryById(id).orElseThrow(ProductDoesNotExistException::new);

        var netPrice = productSummary.getNetPrice();
        var totalPrice = netPrice.add((netPrice.multiply(currentVat))).setScale(2, RoundingMode.HALF_UP);

        return new ProductSummaryDto(productSummary.getId(), productSummary.getName(), productSummary.getShortDescription(),
                totalPrice, productSummary.getIsAvailable(), productSummary.getCreatedAt());
    }

    public ProductDescriptionDto getProductDescriptionById(Long id) {
        var productDescription = productRepository.findDescriptionById(id).orElseThrow(ProductDoesNotExistException::new);

        var netPrice = productDescription.getNetPrice();
        var totalPrice = netPrice.add((netPrice.multiply(currentVat))).setScale(2, RoundingMode.HALF_UP);

        return new ProductDescriptionDto(productDescription.getId(), productDescription.getName(), productDescription.getLongDescription(),
                totalPrice, productDescription.getIsAvailable(), productDescription.getCreatedAt());
    }

    public List<ProductThumbnailDto> getProductThumbnailByName(String name) {
        var productThumbnails = productRepository.findAllByNameContainingIgnoreCase(name);

        List<ProductThumbnailDto> productThumbnailDtoSet = new ArrayList<>();

        for (var productThumbnail : productThumbnails) {
            var netPrice = productThumbnail.getNetPrice();
            var totalPrice = netPrice.add((netPrice.multiply(currentVat))).setScale(2, RoundingMode.HALF_UP);

            productThumbnailDtoSet.add(new ProductThumbnailDto(productThumbnail.getId(), productThumbnail.getName(), totalPrice));
        }

        return productThumbnailDtoSet;
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public AddTagToProductResponse addTagToProduct(Long productId, Integer tagId) {
        var product = productRepository.findById(productId).orElseThrow(ProductDoesNotExistException::new);
        var tag = tagRepository.findById(tagId).orElseThrow(TagDoesNotExistException::new);
        if (product.getTags().contains(tag)) throw new TagAlreadyExistsException("This product already has this tag");

        product.getTags().add(tag);
        productRepository.save(product);

        List<TagDto> tagDtoList = new ArrayList<>();
        for (var productTag : product.getTags()) {
            var tagDto = tagMapper.toTagDto(productTag);
            tagDtoList.add(tagDto);
        }
        return new AddTagToProductResponse(product.getId(), tagDtoList);
    }
}
