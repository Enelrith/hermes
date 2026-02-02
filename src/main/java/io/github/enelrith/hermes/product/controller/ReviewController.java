package io.github.enelrith.hermes.product.controller;

import io.github.enelrith.hermes.product.dto.AddReviewRequest;
import io.github.enelrith.hermes.product.dto.ReviewDto;
import io.github.enelrith.hermes.product.service.ReviewService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
@Validated
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("/{productId}")
    public ResponseEntity<ReviewDto> addReview(@PathVariable @Positive Long productId, @RequestBody @Valid AddReviewRequest request) {
        var reviewDto = reviewService.addReview(productId, request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(reviewDto.id())
                .toUri();
        return ResponseEntity.created(location).body(reviewDto);
    }
}
