package com.example.librarymanagement.service;

import com.example.librarymanagement.constant.BorrowStatus;
import com.example.librarymanagement.domain.dto.common.CommonResponseDto;
import com.example.librarymanagement.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanagement.domain.dto.pagination.PaginationResponseDto;
import com.example.librarymanagement.domain.dto.request.receipt.BorrowReceiptRequestDto;
import com.example.librarymanagement.domain.dto.request.receipt.CreateBorrowReceiptRequestDto;
import com.example.librarymanagement.domain.dto.response.receipt.BorrowReceiptDetailDto;
import com.example.librarymanagement.domain.dto.response.receipt.BorrowReceiptDetailResponseDto;
import com.example.librarymanagement.domain.dto.response.receipt.BorrowReceiptForReaderResponseDto;
import com.example.librarymanagement.domain.dto.response.receipt.BorrowReceiptResponseDto;
import com.example.librarymanagement.domain.entity.BorrowReceipt;

import java.util.Set;

public interface BorrowReceiptService {
    String generateReceiptNumber();

    CommonResponseDto save(BorrowReceiptRequestDto requestDto, String userId);

    CommonResponseDto update(Long id, BorrowReceiptRequestDto requestDto, String userId);

    void updateBorrowStatus(BorrowReceipt borrowReceipt);

    CommonResponseDto delete(Long id, String userId);

    PaginationResponseDto<BorrowReceiptResponseDto> findAll(PaginationFullRequestDto requestDto, BorrowStatus status);

    BorrowReceiptDetailResponseDto findById(Long id);

    BorrowReceiptDetailResponseDto findByCartId(Long id);

    PaginationResponseDto<BorrowReceiptForReaderResponseDto> findByCardNumber(String cardNumber, PaginationFullRequestDto requestDto, BorrowStatus status);

    BorrowReceiptDetailDto findDetailsById(Long id);

    CommonResponseDto cancelReturn(Set<Long> borrowIds, String userId);

    byte[] createPdfForReceipts(CreateBorrowReceiptRequestDto requestDto, String userId);

    byte[] createOverdueListPdf();

    byte[] exportReturnData();
}
