package com.example.librarymanagement.controller;

import com.example.librarymanagement.annotation.CurrentUser;
import com.example.librarymanagement.annotation.RestApiV1;
import com.example.librarymanagement.base.VsResponseUtil;
import com.example.librarymanagement.constant.UrlConstant;
import com.example.librarymanagement.domain.dto.request.library.LibraryConfigRequestDto;
import com.example.librarymanagement.security.CustomUserDetails;
import com.example.librarymanagement.service.SystemSettingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestApiV1
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "System Settings")
public class SystemSettingController {
    SystemSettingService systemSettingService;

    @Operation(summary = "Get Library Configuration")
    @PreAuthorize("hasRole('ROLE_MANAGE_SYSTEM_SETTINGS')")
    @GetMapping(UrlConstant.SystemSetting.GET_LIBRARY_CONFIG)
    public ResponseEntity<?> getLibraryConfig() {
        return VsResponseUtil.success(systemSettingService.getLibraryConfig());
    }

    @Operation(summary = "Update Library Configuration")
    @PreAuthorize("hasRole('ROLE_MANAGE_SYSTEM_SETTINGS')")
    @PutMapping(UrlConstant.SystemSetting.UPDATE_LIBRARY_CONFIG)
    public ResponseEntity<?> updateLibraryConfig(
            @Valid @RequestBody LibraryConfigRequestDto requestDto,
            @CurrentUser CustomUserDetails userDetails
    ) {
        return VsResponseUtil.success(
                systemSettingService.updateLibraryConfig(requestDto, userDetails.getUserId())
        );
    }
}
