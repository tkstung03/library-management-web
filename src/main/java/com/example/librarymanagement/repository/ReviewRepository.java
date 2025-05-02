package com.example.librarymanagement.repository;

import com.example.librarymanagement.domain.dto.response.other.ReviewResponseDto;
import com.example.librarymanagement.domain.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>, JpaSpecificationExecutor<Review> {
    @Query("SELECT new com.example.librarymanagement.domain.dto.response.other.ReviewResponseDto(r) FROM Review r WHERE r.bookDefinition.id = :bookDefinitionId ORDER BY r.lastModifiedDate DESC")
    List<ReviewResponseDto> getReviewsByBook(@Param("bookDefinitionId") Long bookDefinitionId);

    @Query("SELECT new com.example.librarymanagement.domain.dto.response.other.ReviewResponseDto(r) FROM Review r WHERE r.reader.id = :readerId ORDER BY r.lastModifiedDate DESC")
    List<ReviewResponseDto> getReviewsByReader(@Param("readerId") Long readerId);

    boolean existsByBookDefinition_IdAndReader_CardNumber(Long bookDefinitionId, String cardNumber);
}
