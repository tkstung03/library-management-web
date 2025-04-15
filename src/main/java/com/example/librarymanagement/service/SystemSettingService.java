package com.example.librarymanagement.service;

import com.example.librarymanagement.domain.dto.common.CommonResponseDto;
import com.example.librarymanagement.domain.dto.request.library.LibraryConfigRequestDto;
import com.example.librarymanagement.domain.dto.request.library.LibraryInfoRequestDto;
import com.example.librarymanagement.domain.dto.request.library.LibraryRulesRequestDto;
import com.example.librarymanagement.domain.dto.response.library.LibraryConfigResponseDto;
import com.example.librarymanagement.domain.dto.response.library.LibraryInfoResponseDto;
import org.springframework.web.multipart.MultipartFile;

public interface SystemSettingService {
    CommonResponseDto updateLibraryRules(LibraryRulesRequestDto requestDto, String userId);

    String getLibraryRules();

    LibraryConfigResponseDto getLibraryConfig();

    CommonResponseDto updateLibraryConfig(LibraryConfigRequestDto requestDto, String userId);

    LibraryInfoResponseDto getLibraryInfo();

    CommonResponseDto updateLibraryInfo(LibraryInfoRequestDto requestDto, MultipartFile logo, String userId);
}
