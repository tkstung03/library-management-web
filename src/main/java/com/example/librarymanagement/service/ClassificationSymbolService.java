package com.example.librarymanagement.service;

import com.example.librarymanagement.domain.dto.common.CommonResponseDto;
import com.example.librarymanagement.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanagement.domain.dto.pagination.PaginationResponseDto;
import com.example.librarymanagement.domain.dto.request.other.ClassificationSymbolRequestDto;
import com.example.librarymanagement.domain.entity.ClassificationSymbol;

public interface ClassificationSymbolService {
    void init(String classificationSymbolCsvPath);

    CommonResponseDto save(ClassificationSymbolRequestDto requestDto);

    CommonResponseDto update(Long id, ClassificationSymbolRequestDto requestDto);

    CommonResponseDto delete(Long id);

    PaginationResponseDto<ClassificationSymbol> findAll(PaginationFullRequestDto requestDto);

    ClassificationSymbol findById(Long id);

    CommonResponseDto toggleActiveStatus(Long id);
}
