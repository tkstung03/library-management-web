package com.example.librarymanagement.controller;

import com.example.librarymanagement.annotation.RestApiV1;
import com.example.librarymanagement.base.VsResponseUtil;
import com.example.librarymanagement.constant.UrlConstant;
import com.example.librarymanagement.domain.dto.filter.LibraryVisitFilter;
import com.example.librarymanagement.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanagement.domain.dto.request.library.LibraryVisitRequestDto;
import com.example.librarymanagement.domain.dto.response.library.LibraryVisitResponseDto;
import com.example.librarymanagement.service.LibraryVisitService;
import com.example.librarymanagement.service.PdfService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestApiV1
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Library Visit")
public class LibraryVisitController {
    PdfService pdfService;
    LibraryVisitService libraryVisitService;

    @Operation(summary = "API Create Visit")
    @PreAuthorize("hasRole('ROLE_MANAGE_READER')")
    @PostMapping(UrlConstant.LibraryVisit.CREATE)
    public ResponseEntity<?> createVisit(@Valid @RequestBody LibraryVisitRequestDto requestDto) {
        return VsResponseUtil.success(HttpStatus.CREATED, libraryVisitService.save(requestDto));
    }

    @Operation(summary = "API Update Visit")
    @PreAuthorize("hasRole('ROLE_MANAGE_READER')")
    @PutMapping(UrlConstant.LibraryVisit.UPDATE)
    public ResponseEntity<?> updateVisit(
            @PathVariable Long id,
            @Valid @RequestBody LibraryVisitRequestDto requestDto
    ) {
        return VsResponseUtil.success(libraryVisitService.update(id, requestDto));
    }

    @Operation(summary = "API Close Library - Update exit time for all readers")
    @PreAuthorize("hasRole('ROLE_MANAGE_READER')")
    @PostMapping(UrlConstant.LibraryVisit.CLOSE)
    public ResponseEntity<?> closeLibrary() {
        return VsResponseUtil.success(libraryVisitService.closeLibrary());
    }

    @Operation(summary = "API Get Visits")
    @PreAuthorize("hasRole('ROLE_MANAGE_READER')")
    @GetMapping(UrlConstant.LibraryVisit.GET_ALL)
    public ResponseEntity<?> getVisits(
            @ParameterObject PaginationFullRequestDto requestDto,
            @ParameterObject LibraryVisitFilter filter
    ) {
        return VsResponseUtil.success(libraryVisitService.findAll(requestDto, filter));
    }

    @Operation(summary = "API Get Visit By Id")
    @PreAuthorize("hasRole('ROLE_MANAGE_READER')")
    @GetMapping(UrlConstant.LibraryVisit.GET_BY_ID)
    public ResponseEntity<?> getVisitById(@PathVariable Long id) {
        return VsResponseUtil.success(libraryVisitService.findById(id));
    }
    @Operation(summary = "Export PDF thống kê lượt vào - ra thư viện theo khoảng ngày")
    @PreAuthorize("hasRole('ROLE_MANAGE_READER')")
    @GetMapping(UrlConstant.LibraryVisit.EXPORT_PDF)
    public ResponseEntity<byte[]> exportVisitReportToPdf(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        List<LibraryVisitResponseDto> visitLogs =
                libraryVisitService.getVisits(startDate, endDate);

        byte[] pdfBytes = pdfService.generateVisitReportPdf(visitLogs, startDate, endDate);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", "thong_ke_luot_vao_ra.pdf");

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

}
