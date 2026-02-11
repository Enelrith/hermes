package io.github.enelrith.hermes.product.controller;

import io.github.enelrith.hermes.product.dto.*;
import io.github.enelrith.hermes.product.service.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Validated
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductFullDto> addProduct(@RequestBody @Valid AddProductRequest request) {
        var productFullDto = productService.addProduct(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(productFullDto.id())
                .toUri();
        return ResponseEntity.created(location).body(productFullDto);
    }

    @GetMapping("/{id}/summary")
    public ResponseEntity<ProductSummaryDto> getProductSummary(@PathVariable @Positive Long id) {
        var productSummaryDto = productService.getProductSummaryById(id);

        return ResponseEntity.ok(productSummaryDto);
    }

    @GetMapping("/{id}/description")
    public ResponseEntity<ProductDescriptionDto> getProductDescription(@PathVariable @Positive Long id) {
        var productDescriptionDto = productService.getProductDescriptionById(id);

        return ResponseEntity.ok(productDescriptionDto);
    }

    @GetMapping("/thumbnail")
    public ResponseEntity<Page<ProductThumbnailDto>> getProductThumbnails(@Valid ProductFilters filters, Pageable pageable) {

        pageable = PageRequest.of(pageable.getPageNumber(), 10, Sort.by("name").ascending());
        var productThumbnails = productService.getProductThumbnailsBySpecifications(filters, pageable);
        return ResponseEntity.ok(productThumbnails);
    }

    @PostMapping("/{productId}/tags/{tagId}")
    public ResponseEntity<AddTagToProductResponse> addTagToProduct(@PathVariable @Positive Long productId,
                                                                   @PathVariable @Positive Integer tagId) {
        var response = productService.addTagToProduct(productId, tagId);

        return ResponseEntity.ok(response);
    }
}
