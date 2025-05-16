package com.example.librarymanagement.controller;

import com.example.librarymanagement.annotation.CurrentUser;
import com.example.librarymanagement.annotation.RestApiV1;
import com.example.librarymanagement.base.VsResponseUtil;
import com.example.librarymanagement.constant.UrlConstant;
import com.example.librarymanagement.domain.dto.request.library.LibraryInfoRequestDto;
import com.example.librarymanagement.security.CustomUserDetails;
import com.example.librarymanagement.service.SystemSettingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@RestApiV1
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "System settings")
public class LibraryInfoController {
    SystemSettingService systemSettingService;

    @Operation(summary = "Get Library Info")
    @GetMapping(UrlConstant.SystemSetting.GET_LIBRARY_INFO)
    public ResponseEntity<?> getLibraryInfo() {
        return VsResponseUtil.success(systemSettingService.getLibraryInfo());
    }

    @Operation(summary = "Update Library Info")
    @PreAuthorize("hasRole('ROLE_MANAGE_SYSTEM_SETTINGS')")
    @PutMapping(value = UrlConstant.SystemSetting.UPDATE_LIBRARY_INFO, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateLibraryInfo(
            @Valid @ModelAttribute LibraryInfoRequestDto requestDto,
            @RequestParam(value = "logo", required = false) MultipartFile logo,
            @CurrentUser CustomUserDetails userDetails
    ) {
        return VsResponseUtil.success(systemSettingService.updateLibraryInfo(requestDto, logo, userDetails.getUserId()));
    }
}
