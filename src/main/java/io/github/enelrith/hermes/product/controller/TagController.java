package io.github.enelrith.hermes.product.controller;

import io.github.enelrith.hermes.product.dto.AddTagRequest;
import io.github.enelrith.hermes.product.dto.TagDto;
import io.github.enelrith.hermes.product.service.TagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;


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
}
