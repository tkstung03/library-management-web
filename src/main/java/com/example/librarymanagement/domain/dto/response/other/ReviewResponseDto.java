package com.example.librarymanagement.domain.dto.response.other;

import com.example.librarymanagement.domain.dto.common.DateAuditingDto;
import com.example.librarymanagement.domain.dto.response.reader.ReaderBasicResponseDto;
import com.example.librarymanagement.domain.entity.Review;
import lombok.Getter;

@Getter
public class ReviewResponseDto extends DateAuditingDto {

    private final long id;

    private final int rating;

    private final String comment;

    private final ReaderBasicResponseDto reader;

    public ReviewResponseDto(Review review){
        this.createdDate = review.getCreatedDate();
        this.lastModifiedDate = review.getLastModifiedDate();
        this.id = review.getId();
        this.rating = review.getRating();
        this.comment = review.getComment();
        this.reader =new ReaderBasicResponseDto(review.getReader());
    }
}
