package com.example.librarymanagement.service;

import com.example.librarymanagement.domain.dto.pagination.PaginationRequestDto;
import com.example.librarymanagement.domain.dto.response.statistics.*;

import java.util.List;

public interface StatisticsService {
    LibraryStatisticsResponseDto getLibraryStatistics();

    BorrowStatisticsResponseDto getBorrowStatistics();

    LoanStatusResponseDto getLoanStatus();

    List<PublicationResponseDto> getMostBorrowedPublications();

    List<CategoryStatisticsResponseDto> getPublicationCountByCategory(PaginationRequestDto requestDto);
}
