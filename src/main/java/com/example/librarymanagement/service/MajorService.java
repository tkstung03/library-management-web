package com.example.librarymanagement.service;

import com.example.librarymanagement.domain.dto.common.CommonResponseDto;
import com.example.librarymanagement.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanagement.domain.dto.pagination.PaginationResponseDto;
import com.example.librarymanagement.domain.dto.request.major.MajorRequestDto;
import com.example.librarymanagement.domain.dto.response.major.MajorResponseDto;
import com.example.librarymanagement.domain.entity.Major;

public interface MajorService {
    void init(String majorCsvPath);

    CommonResponseDto save(MajorRequestDto requestDto, String userId);

    CommonResponseDto update(Long id, MajorRequestDto requestDto, String userId);

    CommonResponseDto delete(Long id, String userId);

    PaginationResponseDto<MajorResponseDto> findAll(PaginationFullRequestDto requestDto);

    Major findById(Long id);

    CommonResponseDto toggleActiveStatus(Long id, String userId);
}
