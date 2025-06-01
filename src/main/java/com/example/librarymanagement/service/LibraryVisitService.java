package com.example.librarymanagement.service;

import com.example.librarymanagement.domain.dto.common.CommonResponseDto;
import com.example.librarymanagement.domain.dto.filter.LibraryVisitFilter;
import com.example.librarymanagement.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanagement.domain.dto.pagination.PaginationResponseDto;
import com.example.librarymanagement.domain.dto.request.library.LibraryVisitRequestDto;
import com.example.librarymanagement.domain.dto.response.library.LibraryVisitResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface LibraryVisitService {

    CommonResponseDto save(LibraryVisitRequestDto requestDto);

    CommonResponseDto update(Long id, LibraryVisitRequestDto requestDto);

    PaginationResponseDto<LibraryVisitResponseDto> findAll(PaginationFullRequestDto requestDto, LibraryVisitFilter filter);

    LibraryVisitResponseDto findById(Long id);

    List<LibraryVisitResponseDto> getVisits(LocalDate fromDate, LocalDate toDate);

    CommonResponseDto closeLibrary();
}
