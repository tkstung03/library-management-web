package com.example.librarymanagement.service;

import com.example.librarymanagement.domain.dto.common.CommonResponseDto;
import com.example.librarymanagement.domain.dto.request.review.CreateReviewRequestDto;
import com.example.librarymanagement.domain.dto.request.review.UpdateReviewRequestDto;
import com.example.librarymanagement.domain.dto.response.other.ReviewResponseDto;

import java.util.List;

public interface ReviewService {
    List<ReviewResponseDto> getReviewsByBook(Long bookId);

    List<ReviewResponseDto> getReviewsByReader(Long readerId);

    ReviewResponseDto addReview(String cardNumber, CreateReviewRequestDto requestDto);

    ReviewResponseDto updateReview(Long reviewId, UpdateReviewRequestDto requestDto, String cardNumber);

    CommonResponseDto deleteReview(Long reviewId, String cardNumber);
}
