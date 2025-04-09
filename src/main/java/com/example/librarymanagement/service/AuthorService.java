package com.example.librarymanagement.service;

import com.example.librarymanagement.domain.dto.common.CommonResponseDto;
import com.example.librarymanagement.domain.dto.request.author.AuthorRequestDto;

public interface AuthorService {
    void init(String authorsCsvPath);

    CommonResponseDto save(AuthorRequestDto requestDto, String userId);

    CommonResponseDto update(AuthorRequestDto requestDto, Long id, String userId);

    CommonResponseDto delete(Long id,String userId);

    Paginat

}
