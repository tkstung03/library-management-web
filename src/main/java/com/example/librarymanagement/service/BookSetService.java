package com.example.librarymanagement.service;

import com.example.librarymanagement.domain.dto.common.CommonResponseDto;
import com.example.librarymanagement.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanagement.domain.dto.pagination.PaginationResponseDto;
import com.example.librarymanagement.domain.dto.request.book.BookSetRequestDto;
import com.example.librarymanagement.domain.dto.response.bookset.BookSetResponseDto;
import com.example.librarymanagement.domain.entity.BookSet;

public interface BookSetService {
    void init(String bookSetCsvPath);

    CommonResponseDto save(BookSetRequestDto requestDto, String userId);

    CommonResponseDto update(Long id, BookSetRequestDto requestDto, String userId);

    CommonResponseDto delete(Long id, String userId);

    PaginationResponseDto<BookSetResponseDto> findAll(PaginationFullRequestDto requestDto);

    BookSet findById(Long id);

    CommonResponseDto toggleActiveStatus(Long id, String userId);

}
