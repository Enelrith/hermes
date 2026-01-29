package io.github.enelrith.hermes.product.controller;

import io.github.enelrith.hermes.product.dto.*;
import io.github.enelrith.hermes.product.service.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

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

    @GetMapping("/{name}/thumbnail")
    public ResponseEntity<List<ProductThumbnailDto>> getProductThumbnails(@PathVariable
                                                                          @NotBlank
                                                                          @Size(min = 1, max = 50)
                                                                          String name) {
        var productThumbnails = productService.getProductThumbnailByName(name);
        return ResponseEntity.ok(productThumbnails);
    }

    @PostMapping("/{productId}/tags/{tagId}")
    public ResponseEntity<AddTagToProductResponse> addTagToProduct(@PathVariable @Positive Long productId,
                                                                   @PathVariable @Positive Integer tagId) {
        var response = productService.addTagToProduct(productId, tagId);

        return ResponseEntity.ok(response);
    }
}
