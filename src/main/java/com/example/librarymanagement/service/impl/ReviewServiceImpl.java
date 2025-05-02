package com.example.librarymanagement.service.impl;

import com.example.librarymanagement.constant.ErrorMessage;
import com.example.librarymanagement.constant.SuccessMessage;
import com.example.librarymanagement.domain.dto.common.CommonResponseDto;
import com.example.librarymanagement.domain.dto.request.review.CreateReviewRequestDto;
import com.example.librarymanagement.domain.dto.request.review.UpdateReviewRequestDto;
import com.example.librarymanagement.domain.dto.response.other.ReviewResponseDto;
import com.example.librarymanagement.domain.entity.BookDefinition;
import com.example.librarymanagement.domain.entity.Reader;
import com.example.librarymanagement.domain.entity.Review;
import com.example.librarymanagement.exception.BadRequestException;
import com.example.librarymanagement.exception.NotFoundException;
import com.example.librarymanagement.exception.UnauthorizedException;
import com.example.librarymanagement.repository.BookDefinitionRepository;
import com.example.librarymanagement.repository.ReaderRepository;
import com.example.librarymanagement.repository.ReviewRepository;
import com.example.librarymanagement.service.ReviewService;
import com.example.librarymanagement.util.MessageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    private final BookDefinitionRepository bookDefinitionRepository;

    private final ReaderRepository readerRepository;

    private final MessageUtil messageUtil;

    private Review getEntity(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.Review.ERR_NOT_FOUND_ID, reviewId));
    }

    @Override
    public List<ReviewResponseDto> getReviewsByBook(Long bookId) {
        return reviewRepository.getReviewsByBook(bookId);
    }

    @Override
    public List<ReviewResponseDto> getReviewsByReader(Long readerId) {
        return reviewRepository.getReviewsByReader(readerId);
    }

    @Override
    public ReviewResponseDto addReview(String cardNumber, CreateReviewRequestDto requestDto) {
        boolean hasReviewed = reviewRepository.existsByBookDefinition_IdAndReader_CardNumber(requestDto.getBookDefinitionId(), cardNumber);
        if (hasReviewed) {
            throw new BadRequestException(ErrorMessage.Review.ERR_ALREADY_REVIEWED);
        }

        BookDefinition bookDefinition = bookDefinitionRepository.findById(requestDto.getBookDefinitionId())
                .orElseThrow(() -> new NotFoundException(ErrorMessage.BookDefinition.ERR_NOT_FOUND_ID, requestDto.getBookDefinitionId()));

        Reader reader = readerRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.Reader.ERR_NOT_FOUND_CARD_NUMBER, cardNumber));

        Review review = new Review();
        review.setBookDefinition(bookDefinition);
        review.setReader(reader);
        review.setComment(requestDto.getComment());
        review.setRating(requestDto.getRating());

        reviewRepository.save(review);

        return new ReviewResponseDto(review);
    }

    @Override
    public ReviewResponseDto updateReview(Long reviewId, UpdateReviewRequestDto requestDto, String cardNumber) {
        Review review = getEntity(reviewId);

        if (!review.getReader().getCardNumber().equals(cardNumber)) {
            throw new UnauthorizedException(ErrorMessage.ERR_FORBIDDEN_UPDATE_DELETE);
        }

        review.setRating(requestDto.getRating());
        review.setComment(requestDto.getComment());

        reviewRepository.save(review);

        return new ReviewResponseDto(review);
    }

    @Override
    public CommonResponseDto deleteReview(Long reviewId, String cardNumber) {
        Review review = getEntity(reviewId);

        if (cardNumber != null && !review.getReader().getCardNumber().equals(cardNumber)) {
            throw new UnauthorizedException(ErrorMessage.ERR_FORBIDDEN_UPDATE_DELETE);
        }

        reviewRepository.delete(review);

        String message = messageUtil.getMessage(SuccessMessage.DELETE);
        return new CommonResponseDto(message);
    }
}
