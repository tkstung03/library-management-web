package com.example.librarymanagement.service;

import com.example.librarymanagement.domain.dto.common.CommonResponseDto;
import com.example.librarymanagement.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanagement.domain.dto.pagination.PaginationResponseDto;
import com.example.librarymanagement.domain.dto.request.category.CategoryGroupRequestDto;
import com.example.librarymanagement.domain.dto.response.category.CategoryGroupTree;
import com.example.librarymanagement.domain.entity.CategoryGroup;

import java.util.List;

public interface CateforyGroupService {
    void init(String categoryGroupCsvPath);

    CommonResponseDto save(CategoryGroupRequestDto requestDto, String userId);

    CommonResponseDto update(Long id, CategoryGroupRequestDto requestDto, String userId);

    CommonResponseDto delete(Long id, String userId);

    PaginationResponseDto<CategoryGroup> findAll(PaginationFullRequestDto requestDto);

    CategoryGroup findById(Long id);

    CommonResponseDto toggleActiveStatus(Long id, String userId);

    List<CategoryGroupTree> findTree();
}
