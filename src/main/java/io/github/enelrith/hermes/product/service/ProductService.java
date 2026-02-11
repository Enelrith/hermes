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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;
    private final ManufacturerRepository manufacturerRepository;
    private final TagRepository tagRepository;
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

        productRepository.saveAndFlush(product);

        return productMapper.toProductFullDto(product);
    }

    public ProductSummaryDto getProductSummaryById(Long id) {

        return productRepository.findSummaryById(id).orElseThrow(ProductDoesNotExistException::new);
    }

    public ProductDescriptionDto getProductDescriptionById(Long id) {

        return productRepository.findDescriptionById(id).orElseThrow(ProductDoesNotExistException::new);
    }

    public Page<ProductThumbnailDto> getProductThumbnailByName(String name, Pageable pageable) {

        return productRepository.findAllByNameContainingIgnoreCase(name, pageable);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public AddTagToProductResponse addTagToProduct(Long productId, Integer tagId) {
        var product = productRepository.findById(productId).orElseThrow(ProductDoesNotExistException::new);
        var tag = tagRepository.findById(tagId).orElseThrow(TagDoesNotExistException::new);
        if (product.getTags().contains(tag)) throw new TagAlreadyExistsException("This product already has this tag");

        product.getTags().add(tag);
        productRepository.save(product);

        Set<TagDto> tagDtoSet = product.getTags().stream().map(tagMapper::toTagDto).collect(Collectors.toSet());

        return new AddTagToProductResponse(product.getId(), tagDtoSet);
    }
}
