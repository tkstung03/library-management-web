package com.example.librarymanagement.service.impl;

import com.example.librarymanagement.domain.dto.filter.LogFilter;
import com.example.librarymanagement.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanagement.domain.dto.pagination.PaginationResponseDto;
import com.example.librarymanagement.domain.dto.response.other.LogResponseDto;

public interface LogService {
    PaginationResponseDto<LogResponseDto> findAll(PaginationFullRequestDto requestDto, LogFilter logFilter);

    void createLog(String feature, String event, String content, String userId);
}
