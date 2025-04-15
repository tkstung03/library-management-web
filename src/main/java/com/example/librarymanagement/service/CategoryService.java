package com.example.librarymanagement.service;

import com.example.librarymanagement.domain.dto.common.CommonResponseDto;
import com.example.librarymanagement.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanagement.domain.dto.pagination.PaginationResponseDto;
import com.example.librarymanagement.domain.dto.request.category.CategoryRequestDto;
import com.example.librarymanagement.domain.dto.response.category.CategoryResponseDto;
import com.example.librarymanagement.domain.entity.Category;

public interface CategoryService {
    void init(String categoriesCsvPath);

    CommonResponseDto save(CategoryRequestDto requestDto, String userId);

    CommonResponseDto update(Long id, CategoryRequestDto requestDto, String userId);

    CommonResponseDto delete(Long id, String userId);

    PaginationResponseDto<CategoryResponseDto> findAll(PaginationFullRequestDto requestDto);

    Category findById(Long id);

    CommonResponseDto toggleActiveStatus(Long id, String userId);
}
