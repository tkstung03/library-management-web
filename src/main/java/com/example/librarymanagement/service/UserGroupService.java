package com.example.librarymanagement.service;

import com.example.librarymanagement.domain.dto.common.CommonResponseDto;
import com.example.librarymanagement.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanagement.domain.dto.pagination.PaginationResponseDto;
import com.example.librarymanagement.domain.dto.request.user.UserGroupRequestDto;
import com.example.librarymanagement.domain.dto.response.user.UserGroupResponseDto;

public interface UserGroupService {
    void init();

    CommonResponseDto save(UserGroupRequestDto requestDto, String userId);

    CommonResponseDto update(Long id, UserGroupRequestDto requestDto, String userId);

    CommonResponseDto delete(Long id, String userId);

    PaginationResponseDto<UserGroupResponseDto> findAll(PaginationFullRequestDto requestDto);

    UserGroupResponseDto findById(Long id);

    CommonResponseDto toggleActiveStatus(Long id, String userId);
}
