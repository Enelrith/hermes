package io.github.enelrith.hermes.product.controller;

import io.github.enelrith.hermes.product.dto.AddCategoryRequest;
import io.github.enelrith.hermes.product.dto.CategoryDto;
import io.github.enelrith.hermes.product.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryDto> addCategory(@Valid @RequestBody AddCategoryRequest request) {
        var category = categoryService.addCategory(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(category.id())
                .toUri();
        return ResponseEntity.created(location).body(category);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Integer categoryId) {
        var category = categoryService.getCategoryById(categoryId);

        return ResponseEntity.ok(category);
    }
}
