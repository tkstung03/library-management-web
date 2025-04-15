package com.example.librarymanagement.service;

import com.example.librarymanagement.constant.BookBorrowStatus;
import com.example.librarymanagement.domain.dto.common.CommonResponseDto;
import com.example.librarymanagement.domain.dto.filter.TimeFilter;
import com.example.librarymanagement.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanagement.domain.dto.pagination.PaginationResponseDto;
import com.example.librarymanagement.domain.dto.request.book.BookReturnRequestDto;
import com.example.librarymanagement.domain.dto.response.bookborrow.BookBorrowResponseDto;

import java.util.List;
import java.util.Set;

public interface BookBorrowService {
    PaginationResponseDto<BookBorrowResponseDto> findAll(PaginationFullRequestDto requestDto, TimeFilter timeFilter, List<BookBorrowStatus> status);

    CommonResponseDto returnBooks(List<BookReturnRequestDto> requestDtos, String userId);

    CommonResponseDto reportLostBookByIds(Set<Long> ids, String userId);
}
