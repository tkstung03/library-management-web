package com.example.librarymanagement.service;

import com.example.librarymanagement.domain.dto.common.CommonResponseDto;
import com.example.librarymanagement.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanagement.domain.dto.pagination.PaginationResponseDto;
import com.example.librarymanagement.domain.dto.request.user.UserRequestDto;
import com.example.librarymanagement.domain.dto.response.user.UserResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    void init();

    CommonResponseDto save(UserRequestDto requestDto, String userId);

    CommonResponseDto update(String id, UserRequestDto requestDto, String userId);

    CommonResponseDto delete(String id, String userId);

    PaginationResponseDto<UserResponseDto> findAll(PaginationFullRequestDto requestDto);

    UserResponseDto findById(String id);

    List<String> uploadImages(List<MultipartFile> files, String userId);
}
