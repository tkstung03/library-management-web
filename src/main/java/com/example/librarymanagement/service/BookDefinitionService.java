package com.example.librarymanagement.service;

import com.example.librarymanagement.domain.dto.common.CommonResponseDto;
import com.example.librarymanagement.domain.dto.filter.BookDefinitionFilter;
import com.example.librarymanagement.domain.dto.filter.QueryFilter;
import com.example.librarymanagement.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanagement.domain.dto.pagination.PaginationRequestDto;
import com.example.librarymanagement.domain.dto.pagination.PaginationResponseDto;
import com.example.librarymanagement.domain.dto.pagination.PaginationSortRequestDto;
import com.example.librarymanagement.domain.dto.request.book.BookDefinitionRequestDto;
import com.example.librarymanagement.domain.dto.response.bookdefinition.BookByBookDefinitionResponseDto;
import com.example.librarymanagement.domain.dto.response.bookdefinition.BookDefinitionResponseDto;
import com.example.librarymanagement.domain.dto.response.bookdefinition.BookDetailForReaderResponseDto;
import com.example.librarymanagement.domain.dto.response.bookdefinition.BookForReaderResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

public interface BookDefinitionService {
    void init(String bookDefinitionCsvPath);

    CommonResponseDto save(BookDefinitionRequestDto requestDto, MultipartFile image, String userId);

    CommonResponseDto update(Long id, BookDefinitionRequestDto requestDto, MultipartFile image, String userId);

    CommonResponseDto delete(Long id, String userId);

    PaginationResponseDto<BookDefinitionResponseDto> findAll(PaginationFullRequestDto requestDto);

    List<BookDefinitionResponseDto> findByIds(Set<Long> ids);

    BookDefinitionResponseDto findById(Long id);

    CommonResponseDto toggleActiveStatus(Long id, String userId);

    PaginationResponseDto<BookByBookDefinitionResponseDto> getBooks(PaginationFullRequestDto requestDto, Long categoryGroupId, Long categoryId);

    PaginationResponseDto<BookForReaderResponseDto> getBooksForUser(PaginationFullRequestDto requestDto, Long categoryGroupId, Long categoryId, Long authorId, String filterType);

    BookDetailForReaderResponseDto getBookDetailForUser(Long id);

    PaginationResponseDto<BookForReaderResponseDto> advancedSearchBooks(List<QueryFilter> queryFilters, PaginationSortRequestDto requestDto);

    PaginationResponseDto<BookForReaderResponseDto> searchBooks(BookDefinitionFilter filters, PaginationSortRequestDto requestDto);

    byte[] getBooksPdfContent(Set<Long> ids);

    byte[] getBooksLabelType1PdfContent(Set<Long> ids);

    byte[] getBooksLabelType2PdfContent(Set<Long> ids);

    byte[] generateBookListPdf();
}
