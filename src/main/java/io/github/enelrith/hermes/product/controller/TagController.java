package io.github.enelrith.hermes.product.controller;

import io.github.enelrith.hermes.product.dto.AddTagRequest;
import io.github.enelrith.hermes.product.dto.TagDto;
import io.github.enelrith.hermes.product.service.TagService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Set;


@RestController
@RequestMapping("/tags")
@RequiredArgsConstructor
@Validated
public class TagController {
    private final TagService tagService;

    @PostMapping
    public ResponseEntity<TagDto> addTag(@Valid @RequestBody AddTagRequest request) {
        var tag = tagService.addTag(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(tag.id())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TagDto> getTagById(@PathVariable @Positive Integer id) {
        var tag = tagService.getTagById(id);

        return ResponseEntity.ok(tag);
    }

    @GetMapping
    public ResponseEntity<Set<TagDto>> getAllTags() {
        var tags = tagService.getAllTags();

        return ResponseEntity.ok(tags);
    }

    @GetMapping( "/products/{productId}")
    public ResponseEntity<Set<TagDto>> getTagsByProductId(@PathVariable @Positive Long productId) {
        var tags = tagService.getTagsByProductId(productId);

        return ResponseEntity.ok(tags);
    }
}
