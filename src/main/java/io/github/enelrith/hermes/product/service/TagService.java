package io.github.enelrith.hermes.product.service;

import io.github.enelrith.hermes.product.dto.AddTagRequest;
import io.github.enelrith.hermes.product.dto.TagDto;
import io.github.enelrith.hermes.product.exception.ProductDoesNotExistException;
import io.github.enelrith.hermes.product.exception.TagAlreadyExistsException;
import io.github.enelrith.hermes.product.exception.TagDoesNotExistException;
import io.github.enelrith.hermes.product.mapper.TagMapper;
import io.github.enelrith.hermes.product.repository.ProductRepository;
import io.github.enelrith.hermes.product.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;
    private final ProductRepository productRepository;
    private final TagMapper tagMapper;

    @PreAuthorize("hasRole('ADMIN')")
    public TagDto addTag(AddTagRequest request) {
        if (tagRepository.existsByName(request.name())) throw new TagAlreadyExistsException();

        var tag = tagMapper.toEntity(request);
        tagRepository.save(tag);

        return tagMapper.toTagDto(tag);
    }

    public TagDto getTagById(Integer id) {
        var tag = tagRepository.findById(id).orElseThrow(TagDoesNotExistException::new);

        return tagMapper.toTagDto(tag);
    }

    public Set<TagDto> getAllTags() {
        var tags = tagRepository.findAll();

        return tags.stream().map(tagMapper::toTagDto).collect(Collectors.toSet());
    }

    public Set<TagDto> getTagsByProductId(Long productId) {
        if (!productRepository.existsById(productId)) throw new ProductDoesNotExistException();
        var tags = tagRepository.findByProducts_Id(productId);

        return tags.stream().map(tagMapper::toTagDto).collect(Collectors.toSet());
    }
}
