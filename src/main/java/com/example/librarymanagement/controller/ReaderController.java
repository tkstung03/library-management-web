package com.example.librarymanagement.controller;

import com.example.librarymanagement.annotation.CurrentUser;
import com.example.librarymanagement.annotation.RestApiV1;
import com.example.librarymanagement.base.VsResponseUtil;
import com.example.librarymanagement.constant.UrlConstant;
import com.example.librarymanagement.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanagement.domain.dto.request.reader.CreateReaderCardRequestDto;
import com.example.librarymanagement.domain.dto.request.reader.ReaderRequestDto;
import com.example.librarymanagement.security.CustomUserDetails;
import com.example.librarymanagement.service.ReaderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestApiV1
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Reader")
public class ReaderController {
    ReaderService readerService;

    @Operation(summary = "API Create Reader")
    @PreAuthorize("hasRole('ROLE_MANAGE_READER')")
    @PostMapping(value = UrlConstant.Reader.CREATE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createReader(
            @Valid @ModelAttribute ReaderRequestDto requestDto,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @CurrentUser CustomUserDetails userDetails
    ) {
        return VsResponseUtil.success(HttpStatus.CREATED, readerService.save(requestDto, image, userDetails.getUserId()));
    }

    @Operation(summary = "API Update Reader")
    @PreAuthorize("hasRole('ROLE_MANAGE_READER')")
    @PutMapping(value = UrlConstant.Reader.UPDATE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateReader(
            @PathVariable Long id,
            @Valid @ModelAttribute ReaderRequestDto requestDto,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @CurrentUser CustomUserDetails userDetails
    ) {
        return VsResponseUtil.success(readerService.update(id, requestDto, image, userDetails.getUserId()));
    }

    @Operation(summary = "API Delete Reader")
    @PreAuthorize("hasRole('ROLE_MANAGE_READER')")
    @DeleteMapping(UrlConstant.Reader.DELETE)
    public ResponseEntity<?> deleteReader(
            @PathVariable Long id,
            @CurrentUser CustomUserDetails userDetails
    ) {
        return VsResponseUtil.success(readerService.delete(id, userDetails.getUserId()));
    }

    @Operation(summary = "API Get All Readers")
    @PreAuthorize("hasRole('ROLE_MANAGE_READER')")
    @GetMapping(UrlConstant.Reader.GET_ALL)
    public ResponseEntity<?> getAllReaders(@ParameterObject PaginationFullRequestDto requestDto) {
        return VsResponseUtil.success(readerService.findAll(requestDto));
    }

    @Operation(summary = "API Get Reader By Id")
    @PreAuthorize("hasRole('ROLE_MANAGE_READER')")
    @GetMapping(UrlConstant.Reader.GET_BY_ID)
    public ResponseEntity<?> getReaderById(@PathVariable Long id) {
        return VsResponseUtil.success(readerService.findById(id));
    }

    @Operation(summary = "API Get Reader By CardNumber")
    @PreAuthorize("hasRole('ROLE_MANAGE_READER')")
    @GetMapping(UrlConstant.Reader.GET_BY_CARD_NUMBER)
    public ResponseEntity<?> getReaderByCardNumber(@PathVariable String cardNumber) {
        return VsResponseUtil.success(readerService.findByCardNumber(cardNumber));
    }

    @Operation(summary = "API Generate Cards Reader")
    @PreAuthorize("hasRole('ROLE_MANAGE_READER')")
    @PostMapping(UrlConstant.Reader.PRINT_CARDS)
    public ResponseEntity<byte[]> generateReaderCards(@Valid @RequestBody CreateReaderCardRequestDto requestDto) {
        byte[] pdfBytes = readerService.generateReaderCards(requestDto);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=readers.pdf");
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE);

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    @Operation(summary = "API Get Reader Details")
    @PreAuthorize("hasRole('ROLE_READER')")
    @GetMapping(UrlConstant.Reader.GET_DETAILS)
    public ResponseEntity<?> getReaderDetails(@CurrentUser CustomUserDetails userDetails) {
        return VsResponseUtil.success(readerService.getReaderDetailsByCardNumber(userDetails.getCardNumber()));
    }
}
