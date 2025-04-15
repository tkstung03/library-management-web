package com.example.librarymanagement.service;

import com.example.librarymanagement.domain.dto.common.CommonResponseDto;
import com.example.librarymanagement.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanagement.domain.dto.pagination.PaginationResponseDto;
import com.example.librarymanagement.domain.dto.request.reader.CreateReaderCardRequestDto;
import com.example.librarymanagement.domain.dto.request.reader.ReaderRequestDto;
import com.example.librarymanagement.domain.dto.response.reader.ReaderDetailResponseDto;
import com.example.librarymanagement.domain.dto.response.reader.ReaderResponseDto;
import org.springframework.web.multipart.MultipartFile;

public interface ReaderService {
    void init(String readerCsvPath);

    CommonResponseDto save(ReaderRequestDto requestDto, MultipartFile image, String userId);

    CommonResponseDto update(Long id, ReaderRequestDto requestDto, MultipartFile image, String userId);

    CommonResponseDto delete(Long id, String userId);

    PaginationResponseDto<ReaderResponseDto> findAll(PaginationFullRequestDto requestDto);

    ReaderResponseDto findById(Long id);

    ReaderResponseDto findByCardNumber(String cardNumber);

    byte[] generateReaderCards(CreateReaderCardRequestDto requestDto);

    ReaderDetailResponseDto getReaderDetailsByCardNumber(String cardNumber);
}
