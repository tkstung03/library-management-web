package com.example.librarymanagement.service;

import com.example.librarymanagement.constant.BookCondition;
import com.example.librarymanagement.constant.BookStatus;
import com.example.librarymanagement.domain.dto.common.CommonResponseDto;
import com.example.librarymanagement.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanagement.domain.dto.pagination.PaginationResponseDto;
import com.example.librarymanagement.domain.dto.response.book.BookResponseDto;

import java.util.List;
import java.util.Set;

public interface BookService {

    CommonResponseDto updateStatus(Long id, BookStatus status, String userId);

    PaginationResponseDto<BookResponseDto> findAll(PaginationFullRequestDto requestDto, BookCondition bookCondition);

    List<BookResponseDto> findByIds(Set<Long> ids);

    List<BookResponseDto> findByCodes(Set<String> codes);

    BookResponseDto findById(Long id);

    byte[] getBooksPdfContent(Set<Long> ids);

    byte[] getBooksLabelType1PdfContent(Set<Long> ids);

    byte[] getBooksLabelType2PdfContent(Set<Long> ids);

    byte[] generateBookListPdf();
}
