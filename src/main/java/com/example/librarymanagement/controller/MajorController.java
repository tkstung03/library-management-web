package com.example.librarymanagement.controller;

import com.example.librarymanagement.annotation.CurrentUser;
import com.example.librarymanagement.annotation.RestApiV1;
import com.example.librarymanagement.base.VsResponseUtil;
import com.example.librarymanagement.constant.UrlConstant;
import com.example.librarymanagement.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanagement.domain.dto.request.major.MajorRequestDto;
import com.example.librarymanagement.security.CustomUserDetails;
import com.example.librarymanagement.service.MajorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestApiV1
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Major")
public class MajorController {

    MajorService majorService;

    @Operation(summary = "API Create Major")
    @PreAuthorize("hasRole('ROLE_MANAGE_READER')")
    @PostMapping(UrlConstant.Major.CREATE)
    public ResponseEntity<?> createMajor(
            @Valid @RequestBody MajorRequestDto requestDto,
            @CurrentUser CustomUserDetails userDetails
    ) {
        return VsResponseUtil.success(HttpStatus.CREATED, majorService.save(requestDto, userDetails.getUserId()));
    }

    @Operation(summary = "API Update Major")
    @PreAuthorize("hasRole('ROLE_MANAGE_READER')")
    @PutMapping(UrlConstant.Major.UPDATE)
    public ResponseEntity<?> updateMajor(
            @PathVariable Long id,
            @Valid @RequestBody MajorRequestDto requestDto,
            @CurrentUser CustomUserDetails userDetails
    ) {
        return VsResponseUtil.success(majorService.update(id, requestDto, userDetails.getUserId()));
    }

    @Operation(summary = "API Delete Major")
    @PreAuthorize("hasRole('ROLE_MANAGE_READER')")
    @DeleteMapping(UrlConstant.Major.DELETE)
    public ResponseEntity<?> deleteMajor(
            @PathVariable Long id,
            @CurrentUser CustomUserDetails userDetails
    ) {
        return VsResponseUtil.success(majorService.delete(id, userDetails.getUserId()));
    }

    @Operation(summary = "API Get All Majors")
    @PreAuthorize("hasRole('ROLE_MANAGE_READER')")
    @GetMapping(UrlConstant.Major.GET_ALL)
    public ResponseEntity<?> getAllMajors(@ParameterObject PaginationFullRequestDto requestDto) {
        return VsResponseUtil.success(majorService.findAll(requestDto));
    }

    @Operation(summary = "API Get Major By Id")
    @PreAuthorize("hasRole('ROLE_MANAGE_READER')")
    @GetMapping(UrlConstant.Major.GET_BY_ID)
    public ResponseEntity<?> getMajorById(@PathVariable Long id) {
        return VsResponseUtil.success(majorService.findById(id));
    }

    @Operation(summary = "API Toggle Active Status of Major")
    @PreAuthorize("hasRole('ROLE_MANAGE_READER')")
    @PatchMapping(UrlConstant.Major.TOGGLE_ACTIVE)
    public ResponseEntity<?> toggleActiveStatus(
            @PathVariable Long id,
            @CurrentUser CustomUserDetails userDetails
    ) {
        return VsResponseUtil.success(majorService.toggleActiveStatus(id, userDetails.getUserId()));
    }
}
