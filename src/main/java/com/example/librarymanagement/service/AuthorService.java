package com.example.librarymanagement.service;

import com.example.librarymanagement.domain.dto.common.CommonResponseDto;
import com.example.librarymanagement.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanagement.domain.dto.pagination.PaginationResponseDto;
import com.example.librarymanagement.domain.dto.request.author.AuthorRequestDto;
import com.example.librarymanagement.domain.entity.Author;

public interface AuthorService {
    void init(String authorsCsvPath);

    CommonResponseDto save(AuthorRequestDto requestDto, String userId);

    CommonResponseDto update(Long id, AuthorRequestDto requestDto, String userId);

    CommonResponseDto delete(Long id,String userId);

    PaginationResponseDto<Author> findAll(PaginationFullRequestDto requestDto);

    Author findById(Long id);

    CommonResponseDto toggleActivityStatus(Long id, String userId);

}
