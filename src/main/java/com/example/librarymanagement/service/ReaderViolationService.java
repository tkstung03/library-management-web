package com.example.librarymanagement.service;

import com.example.librarymanagement.domain.dto.common.CommonResponseDto;
import com.example.librarymanagement.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanagement.domain.dto.pagination.PaginationResponseDto;
import com.example.librarymanagement.domain.dto.request.reader.ReaderViolationRequestDto;
import com.example.librarymanagement.domain.dto.response.reader.ReaderViolationResponseDto;

public interface ReaderViolationService {

    CommonResponseDto save(ReaderViolationRequestDto requestDto, String userId);

    CommonResponseDto update(Long id, ReaderViolationRequestDto requestDto, String userId);

    CommonResponseDto delete(Long id, String userId);

    PaginationResponseDto<ReaderViolationResponseDto> findAll(PaginationFullRequestDto requestDto);

    ReaderViolationResponseDto findById(Long id);
}
