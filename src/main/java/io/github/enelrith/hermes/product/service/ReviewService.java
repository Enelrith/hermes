package io.github.enelrith.hermes.product.service;

import io.github.enelrith.hermes.product.dto.AddReviewRequest;
import io.github.enelrith.hermes.product.dto.ReviewDto;
import io.github.enelrith.hermes.product.exception.ProductDoesNotExistException;
import io.github.enelrith.hermes.product.exception.ReviewAlreadyExistsException;
import io.github.enelrith.hermes.product.mapper.ReviewMapper;
import io.github.enelrith.hermes.product.repository.ProductRepository;
import io.github.enelrith.hermes.product.repository.ReviewRepository;
import io.github.enelrith.hermes.security.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewService {
    private final AuthService authService;
    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;

    @Transactional
    public ReviewDto addReview(Long productId, AddReviewRequest request) {
        var user = authService.getCurrentUser();
        var review = reviewRepository.findByProduct_Id(productId).orElse(null);
        if (review != null) throw new ReviewAlreadyExistsException();
        var product = productRepository.findById(productId).orElseThrow(ProductDoesNotExistException::new);

        review = reviewMapper.toEntity(request);
        review.setProduct(product);
        review.setUser(user);

        reviewRepository.saveAndFlush(review);

        return reviewMapper.toReviewDto(review);
    }

    public Page<ReviewDto> getReviewsByUserId(Long userId, Pageable pageable) {
        var reviews = reviewRepository.findAllByUser_Id(userId, pageable);

        return reviews.map(reviewMapper::toReviewDto);
    }

    public Page<ReviewDto> getReviewsByProductId(Long productId, Pageable pageable) {
        var reviews = reviewRepository.findAllByProduct_Id(productId, pageable);

        return reviews.map(reviewMapper::toReviewDto);
    }
}
