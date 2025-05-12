package com.example.librarymanagement.controller;

import com.example.librarymanagement.annotation.RestApiV1;
import com.example.librarymanagement.base.VsResponseUtil;
import com.example.librarymanagement.constant.UrlConstant;
import com.example.librarymanagement.domain.dto.filter.LogFilter;
import com.example.librarymanagement.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanagement.service.LogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;

@RestApiV1
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Log")
public class LogController {
    LogService logService;

    @Operation(summary = "API Get All Logs")
    @PreAuthorize("hasRole('ROLE_MANAGE_LOG')")
    @GetMapping(UrlConstant.Log.GET_ALL)
    public ResponseEntity<?> getAllLogs(
            @ParameterObject PaginationFullRequestDto requestDto,
            @ParameterObject LogFilter logFilter
    ) {
        return VsResponseUtil.success(logService.findAll(requestDto, logFilter));
    }
}
