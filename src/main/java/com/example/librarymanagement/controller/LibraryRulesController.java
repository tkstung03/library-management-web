package com.example.librarymanagement.controller;

import com.example.librarymanagement.annotation.CurrentUser;
import com.example.librarymanagement.annotation.RestApiV1;
import com.example.librarymanagement.base.VsResponseUtil;
import com.example.librarymanagement.constant.UrlConstant;
import com.example.librarymanagement.domain.dto.request.library.LibraryRulesRequestDto;
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
@Tag(name = "Library Rules")
public class LibraryRulesController {
    SystemSettingService systemSettingService;

    @Operation(summary = "API Update Library Rules")
    @PreAuthorize("hasRole('ROLE_MANAGE_SYSTEM_SETTINGS')")
    @PutMapping(UrlConstant.SystemSetting.UPDATE_LIBRARY_RULES)
    public ResponseEntity<?> updateLibraryRules(
            @Valid @RequestBody LibraryRulesRequestDto requestDto,
            @CurrentUser CustomUserDetails userDetails
    ) {
        return VsResponseUtil.success(systemSettingService.updateLibraryRules(requestDto, userDetails.getUserId()));
    }

    @Operation(summary = "API Get Library Rules")
    @GetMapping(UrlConstant.SystemSetting.GET_LIBRARY_RULES)
    public ResponseEntity<?> getLibraryRules() {
        return VsResponseUtil.success(systemSettingService.getLibraryRules());
    }
}
