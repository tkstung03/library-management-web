package com.example.librarymanagement.controller;

import com.example.librarymanagement.annotation.RestApiV1;
import com.example.librarymanagement.base.VsResponseUtil;
import com.example.librarymanagement.constant.UrlConstant;
import com.example.librarymanagement.domain.dto.pagination.PaginationRequestDto;
import com.example.librarymanagement.service.StatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;

@RestApiV1
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Statistic")
public class StatsController {
    private final StatisticsService statisticsService;

    @Operation(summary = "Get Library Statistics")
    @GetMapping(UrlConstant.Stats.GET_LIBRARY_STATISTICS)
    public ResponseEntity<?> getLibraryStatistics() {
        return VsResponseUtil.success(statisticsService.getLibraryStatistics());

    }

    @Operation(summary = "Get Borrow Statistics")
    @PreAuthorize("!hasRole('ROLE_READER')")
    @GetMapping(UrlConstant.Stats.GET_BORROW_STATISTICS)
    public ResponseEntity<?> getBorrowStatistics() {
        return VsResponseUtil.success(statisticsService.getBorrowStatistics());
    }

    @Operation(summary = "Get Loan Status")
    @PreAuthorize("!hasRole('ROLE_READER')")
    @GetMapping(UrlConstant.Stats.GET_LOAN_STATUS)
    public ResponseEntity<?> getLoanStatus() {
        return VsResponseUtil.success(statisticsService.getLoanStatus());
    }

    @Operation(summary = "Get Most Borrowed Publications")
    @PreAuthorize("!hasRole('ROLE_READER')")
    @GetMapping(UrlConstant.Stats.GET_MOST_BORROWED)
    public ResponseEntity<?> getMostBorrowedPublications() {
        return VsResponseUtil.success(statisticsService.getMostBorrowedPublications());
    }

    @Operation(summary = "Get Publication Statistics by Category")
    @PreAuthorize("!hasRole('ROLE_READER')")
    @GetMapping(UrlConstant.Stats.GET_PUBLICATION_STATISTICS)
    public ResponseEntity<?> getPublicationStatisticsByCategory(@ParameterObject PaginationRequestDto requestDto) {
        return VsResponseUtil.success(statisticsService.getPublicationCountByCategory(requestDto));
    }
}
