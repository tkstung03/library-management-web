package com.example.librarymanagement.service;

import com.example.librarymanagement.domain.dto.common.CommonResponseDto;
import com.example.librarymanagement.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanagement.domain.dto.pagination.PaginationResponseDto;
import com.example.librarymanagement.domain.dto.request.receipt.ExportReceiptRequestDto;
import com.example.librarymanagement.domain.dto.response.receipt.ExportReceiptResponseDto;

public interface ExportReceiptService {

    String generateReceiptNumber();

    CommonResponseDto save(ExportReceiptRequestDto requestDto, String userId);

    CommonResponseDto update(Long id, ExportReceiptRequestDto requestDto, String userId);

    CommonResponseDto delete(Long id, String userId);

    PaginationResponseDto<ExportReceiptResponseDto> findAll(PaginationFullRequestDto requestDto);

    ExportReceiptResponseDto findById(Long id);
}
