package com.example.librarymanagement.service;

import com.example.librarymanagement.domain.dto.common.CommonResponseDto;
import com.example.librarymanagement.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanagement.domain.dto.pagination.PaginationResponseDto;
import com.example.librarymanagement.domain.dto.request.publisher.PublisherRequestDto;
import com.example.librarymanagement.domain.entity.Publisher;

public interface PublisherService {
    void init(String publishersCsvPath);

    CommonResponseDto save(PublisherRequestDto requestDto);

    CommonResponseDto update(Long id, PublisherRequestDto requestDto);

    CommonResponseDto delete(Long id);

    PaginationResponseDto<Publisher> findAll(PaginationFullRequestDto requestDto);

    Publisher findById(Long id);

    CommonResponseDto toggleActiveStatus(Long id);

}
