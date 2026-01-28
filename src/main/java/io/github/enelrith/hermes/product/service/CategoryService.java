package io.github.enelrith.hermes.product.service;

import io.github.enelrith.hermes.product.dto.AddCategoryRequest;
import io.github.enelrith.hermes.product.dto.CategoryDto;
import io.github.enelrith.hermes.product.exception.CategoryAlreadyExistsException;
import io.github.enelrith.hermes.product.exception.CategoryDoesNotExistException;
import io.github.enelrith.hermes.product.mapper.CategoryMapper;
import io.github.enelrith.hermes.product.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @PreAuthorize("hasRole('ADMIN')")
    public CategoryDto addCategory(AddCategoryRequest request) {
        if (categoryRepository.existsByName(request.name())) throw new CategoryAlreadyExistsException();

        var category = categoryMapper.toEntity(request);
        categoryRepository.save(category);

        return categoryMapper.toCategoryDto(category);
    }

    public CategoryDto getCategoryById(Integer categoryId) {
        var category = categoryRepository.findById(categoryId).orElseThrow(CategoryDoesNotExistException::new);

        return categoryMapper.toCategoryDto(category);
    }
}
