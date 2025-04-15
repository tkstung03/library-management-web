package com.example.librarymanagement.service;

import com.example.librarymanagement.domain.dto.common.CommonResponseDto;
import com.example.librarymanagement.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanagement.domain.dto.pagination.PaginationResponseDto;
import com.example.librarymanagement.domain.dto.request.receipt.ImportReceiptRequestDto;
import com.example.librarymanagement.domain.dto.response.receipt.ImportReceiptResponseDto;

public interface ImportReceiptService {

    String generateReceiptNumber();

    CommonResponseDto save(ImportReceiptRequestDto requestDto, String userId);

    CommonResponseDto update(Long id, ImportReceiptRequestDto requestDto, String userId);

    CommonResponseDto delete(Long id, String userId);

    PaginationResponseDto<ImportReceiptResponseDto> findAll(PaginationFullRequestDto requestDto);

    ImportReceiptResponseDto findById(Long id);
}
