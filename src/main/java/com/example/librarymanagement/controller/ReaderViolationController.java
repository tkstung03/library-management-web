package com.example.librarymanagement.controller;

import com.example.librarymanagement.annotation.CurrentUser;
import com.example.librarymanagement.annotation.RestApiV1;
import com.example.librarymanagement.base.VsResponseUtil;
import com.example.librarymanagement.constant.UrlConstant;
import com.example.librarymanagement.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanagement.domain.dto.request.reader.ReaderViolationRequestDto;
import com.example.librarymanagement.security.CustomUserDetails;
import com.example.librarymanagement.service.ReaderViolationService;
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
@Tag(name = "Reader Violation")
public class ReaderViolationController {
    ReaderViolationService readerViolationService;

    @Operation(summary = "API Create Reader Violation")
    @PreAuthorize("hasRole('ROLE_MANAGE_READER')")
    @PostMapping(UrlConstant.ReaderViolation.CREATE)
    public ResponseEntity<?> createReaderViolation(
            @Valid @RequestBody ReaderViolationRequestDto requestDto,
            @CurrentUser CustomUserDetails userDetails
    ) {
        return VsResponseUtil.success(HttpStatus.CREATED, readerViolationService.save(requestDto, userDetails.getUserId()));
    }

    @Operation(summary = "API Update Reader Violation")
    @PreAuthorize("hasRole('ROLE_MANAGE_READER')")
    @PutMapping(UrlConstant.ReaderViolation.UPDATE)
    public ResponseEntity<?> updateReaderViolation(
            @PathVariable Long id,
            @Valid @RequestBody ReaderViolationRequestDto requestDto,
            @CurrentUser CustomUserDetails userDetails
    ) {
        return VsResponseUtil.success(readerViolationService.update(id, requestDto, userDetails.getUserId()));
    }

    @Operation(summary = "API Delete Reader Violation")
    @PreAuthorize("hasRole('ROLE_MANAGE_READER')")
    @DeleteMapping(UrlConstant.ReaderViolation.DELETE)
    public ResponseEntity<?> deleteReaderViolation(
            @PathVariable Long id,
            @CurrentUser CustomUserDetails userDetails
    ) {
        return VsResponseUtil.success(readerViolationService.delete(id, userDetails.getUserId()));
    }

    @Operation(summary = "API Get Reader Violations")
    @PreAuthorize("hasAnyRole('ROLE_MANAGE_READER')")
    @GetMapping(UrlConstant.ReaderViolation.GET_ALL)
    public ResponseEntity<?> getReaderViolations(@ParameterObject PaginationFullRequestDto requestDto) {
        return VsResponseUtil.success(readerViolationService.findAll(requestDto));
    }

    @Operation(summary = "API Get Reader Violation By Id")
    @PreAuthorize("hasRole('ROLE_MANAGE_READER')")
    @GetMapping(UrlConstant.ReaderViolation.GET_BY_ID)
    public ResponseEntity<?> getReaderViolationById(@PathVariable Long id) {
        return VsResponseUtil.success(readerViolationService.findById(id));
    }
}
