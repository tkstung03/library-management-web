package com.example.librarymanagement.controller;

import com.example.librarymanagement.annotation.CurrentUser;
import com.example.librarymanagement.annotation.RestApiV1;
import com.example.librarymanagement.base.VsResponseUtil;
import com.example.librarymanagement.constant.BookBorrowStatus;
import com.example.librarymanagement.constant.ErrorMessage;
import com.example.librarymanagement.constant.UrlConstant;
import com.example.librarymanagement.domain.dto.filter.TimeFilter;
import com.example.librarymanagement.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanagement.domain.dto.request.book.BookReturnRequestDto;
import com.example.librarymanagement.security.CustomUserDetails;
import com.example.librarymanagement.service.BookBorrowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestApiV1
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Book Borrow")
public class BookBorrowController {

    BookBorrowService bookBorrowService;

    @Operation(summary = "API Return Books by List of IDs")
    @PreAuthorize("hasRole('ROLE_MANAGE_BORROW_RECEIPT')")
    @PutMapping(UrlConstant.BookBorrow.RETURN_BOOKS)
    public ResponseEntity<?> returnBooks(
            @Valid @RequestBody
            @NotNull(message = ErrorMessage.INVALID_ARRAY_IS_REQUIRED)
            @Size(min = 1, message = ErrorMessage.INVALID_ARRAY_LENGTH)
            List<BookReturnRequestDto> requestDtoList,
            @CurrentUser CustomUserDetails userDetails) {
        return VsResponseUtil.success(bookBorrowService.returnBooks(requestDtoList, userDetails.getUserId()));
    }

    @Operation(summary = "API Report Lost Books by List of IDs")
    @PreAuthorize("hasRole('ROLE_MANAGE_BORROW_RECEIPT')")
    @PutMapping(UrlConstant.BookBorrow.REPORT_LOST)
    public ResponseEntity<?> reportLostBooks(
            @Valid @RequestBody
            @NotNull(message = ErrorMessage.INVALID_ARRAY_IS_REQUIRED)
            @Size(min = 1, message = ErrorMessage.INVALID_ARRAY_LENGTH)
            Set<@NotNull(message = ErrorMessage.INVALID_SOME_THING_FIELD_IS_REQUIRED) Long> ids,
            @CurrentUser CustomUserDetails userDetails
    ) {
        return VsResponseUtil.success(bookBorrowService.reportLostBookByIds(ids, userDetails.getUserId()));
    }

    @Operation(summary = "API Get all Borrow Books")
    @PreAuthorize("hasRole('ROLE_MANAGE_BORROW_RECEIPT')")
    @GetMapping(UrlConstant.BookBorrow.GET_ALL)
    public ResponseEntity<?> getAllBorrowBooks(
            @ParameterObject PaginationFullRequestDto requestDto,
            @ParameterObject TimeFilter timeFilter,
            @RequestParam(value = "status", required = false) List<BookBorrowStatus> statuses
            ) {
        return VsResponseUtil.success(bookBorrowService.findAll(requestDto,timeFilter,statuses));
    }
}
