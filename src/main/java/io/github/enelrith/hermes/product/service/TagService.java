package io.github.enelrith.hermes.product.service;

import io.github.enelrith.hermes.product.dto.AddTagRequest;
import io.github.enelrith.hermes.product.dto.TagDto;
import io.github.enelrith.hermes.product.exception.TagAlreadyExistsException;
import io.github.enelrith.hermes.product.mapper.TagMapper;
import io.github.enelrith.hermes.product.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    @PreAuthorize("hasRole('ADMIN')")
    public TagDto addTag(AddTagRequest request) {
        if (tagRepository.existsByName(request.name())) throw new TagAlreadyExistsException();

        var tag = tagMapper.toEntity(request);
        tagRepository.save(tag);

        return tagMapper.toTagDto(tag);
    }
}
