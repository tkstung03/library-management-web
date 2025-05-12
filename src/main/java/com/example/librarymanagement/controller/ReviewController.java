package com.example.librarymanagement.controller;

import com.example.librarymanagement.annotation.CurrentUser;
import com.example.librarymanagement.annotation.RestApiV1;
import com.example.librarymanagement.base.VsResponseUtil;
import com.example.librarymanagement.constant.UrlConstant;
import com.example.librarymanagement.domain.dto.request.review.CreateReviewRequestDto;
import com.example.librarymanagement.domain.dto.request.review.UpdateReviewRequestDto;
import com.example.librarymanagement.security.CustomUserDetails;
import com.example.librarymanagement.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestApiV1
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Review")
public class ReviewController {
    ReviewService reviewService;

    @Operation(summary = "Get reviews by book ID")
    @GetMapping(UrlConstant.Review.GET_BY_BOOK)
    public ResponseEntity<?> getReviewsByBook(@PathVariable Long bookId) {
        return VsResponseUtil.success(reviewService.getReviewsByBook(bookId));
    }

    @Operation(summary = "Get reviews by reader ID")
    @PreAuthorize("hasRole('ROLE_MANAGE_REVIEW')")
    @GetMapping(UrlConstant.Review.GET_BY_READER_ID)
    public ResponseEntity<?> getReviewsByUser(@PathVariable Long readerId) {
        return VsResponseUtil.success(reviewService.getReviewsByReader(readerId));
    }

    @Operation(summary = "Create a new review")
    @PreAuthorize("hasRole('ROLE_READER')")
    @PostMapping(UrlConstant.Review.CREATE)
    public ResponseEntity<?> addReview(@RequestBody CreateReviewRequestDto requestDto,
                                       @CurrentUser CustomUserDetails userDetails) {
        return VsResponseUtil.success(HttpStatus.CREATED, reviewService.addReview(userDetails.getCardNumber(), requestDto));
    }

    @Operation(summary = "Update a review")
    @PreAuthorize("hasRole('ROLE_READER')")
    @PutMapping(UrlConstant.Review.UPDATE)
    public ResponseEntity<?> updateReview(
            @PathVariable Long reviewId,
            @RequestBody UpdateReviewRequestDto requestDto,
            @CurrentUser CustomUserDetails userDetails
    ) {
        return VsResponseUtil.success(reviewService.updateReview(reviewId, requestDto, userDetails.getCardNumber()));
    }

    @Operation(summary = "Delete a review")
    @PreAuthorize("hasAnyRole('ROLE_MANAGE_REVIEW', 'ROLE_READER')")
    @DeleteMapping(UrlConstant.Review.DELETE)
    public ResponseEntity<?> deleteReview(
            @PathVariable Long reviewId,
            @CurrentUser CustomUserDetails userDetails
    ) {
        return VsResponseUtil.success(reviewService.deleteReview(reviewId, userDetails.getCardNumber()));
    }
}
